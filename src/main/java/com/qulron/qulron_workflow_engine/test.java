package com.qulron.qulron_workflow_engine;/*
Workflow Engine — Single-file Java example
- Purpose: A compact, self-contained example of a configurable workflow engine that executes
  "process objects" (workflows) composed of steps. Each step can run an action (calculate,
  compare, query) and decide next step via labels.
- How to use: paste this into a file WorkflowEngine.java and run with Java 17+.
- Notes: Persistence and real DB integration are abstracted via interfaces for simplicity.
*/

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

// ---- Domain models ----
class test {

}
class Workflow {
    public final String id;
    public final String name;
    public final List<Step> steps;
    public Workflow(String id, String name, List<Step> steps) {
        this.id = id; this.name = name; this.steps = Collections.unmodifiableList(steps);
    }
}

class Step {
    public final String label; // unique label
    public final Action action;
    public final String onSuccess; // label to jump to on success (or null to end)
    public final String onFail;    // label to jump to on fail (or null to end)
    public Step(String label, Action action, String onSuccess, String onFail) {
        this.label = label; this.action = action; this.onSuccess = onSuccess; this.onFail = onFail;
    }
}

// Execution result
class ActionResult {
    public final boolean success;
    public final Map<String, Object> outputs;
    public ActionResult(boolean success) { this(success, Map.of()); }
    public ActionResult(boolean success, Map<String,Object> outputs) { this.success = success; this.outputs = outputs; }
}

// Field store for a workflow execution (transient session-state)
class FieldStore {
    private final Map<String,Object> data = new HashMap<>();
    public void put(String key, Object value) { data.put(key, value); }
    public <T> T get(String key, Class<T> cls) {
        Object v = data.get(key);
        if (v == null) return null;
        return cls.cast(v);
    }
    public Object get(String key) { return data.get(key); }
    public Map<String,Object> snapshot() { return Collections.unmodifiableMap(new HashMap<>(data)); }
}

// ---- Actions ----
abstract class Action {
    public final String name;
    protected Action(String name){ this.name = name; }
    public abstract ActionResult execute(FieldStore fields, EngineContext ctx) throws Exception;
}

class CalculateAction extends Action {
    // a small DSL implemented as a lambda for demo purposes
    private final Function<FieldStore, Map<String,Object>> calcFn;
    public CalculateAction(String name, Function<FieldStore, Map<String,Object>> calcFn){
        super(name); this.calcFn = calcFn;
    }
    @Override
    public ActionResult execute(FieldStore fields, EngineContext ctx) {
        Map<String,Object> outs = calcFn.apply(fields);
        outs.forEach(fields::put);
        return new ActionResult(true, outs);
    }
}

class CompareAction extends Action {
    private final BiFunction<FieldStore, EngineContext, Boolean> comparator;
    public CompareAction(String name, BiFunction<FieldStore, EngineContext, Boolean> comparator){
        super(name); this.comparator = comparator;
    }
    @Override
    public ActionResult execute(FieldStore fields, EngineContext ctx) {
        boolean ok = comparator.apply(fields, ctx);
        return new ActionResult(ok);
    }
}

class SqlAction extends Action {
    private final String sql; // for demo only
    public SqlAction(String name, String sql) { super(name); this.sql = sql; }
    @Override
    public ActionResult execute(FieldStore fields, EngineContext ctx) throws Exception {
        // very simple templating of fields into sql (do NOT use in prod)
        String bound = sql;
        for (Map.Entry<String,Object> e : fields.snapshot().entrySet()){
            bound = bound.replace("{{"+e.getKey()+"}}", String.valueOf(e.getValue()));
        }
        // execute via DataAccess (mocked)
        Object result = ctx.dataAccess.execute(sql, fields.snapshot());
        Map<String,Object> out = new HashMap<>();
        out.put("sql_result", result);
        return new ActionResult(true, out);
    }
}

// ---- Engine and context ----

interface DataAccess {
    // execute SQL or stored proc, return result object (rows, affected count, scalar, etc)
    Object execute(String sqlOrKey, Map<String,Object> bindings) throws Exception;
}

class MockDataAccess implements DataAccess {
    @Override
    public Object execute(String sqlOrKey, Map<String,Object> bindings) {
        // for demo, echo back
        return Map.of("query", sqlOrKey, "bindings", bindings);
    }
}

class EngineContext {
    public final DataAccess dataAccess;
    public final Map<String,Object> env; // global context
    public EngineContext(DataAccess dataAccess) { this.dataAccess = dataAccess; this.env = new HashMap<>(); }
}

class ExecutionTraceEntry {
    public final String stepLabel;
    public final String actionName;
    public final boolean success;
    public final Map<String,Object> fieldSnapshot;
    public ExecutionTraceEntry(String stepLabel, String actionName, boolean success, Map<String,Object> snapshot){
        this.stepLabel = stepLabel; this.actionName = actionName; this.success = success; this.fieldSnapshot = snapshot;
    }
}

class ExecutionResult {
    public final boolean completedNormally;
    public final List<ExecutionTraceEntry> trace;
    public ExecutionResult(boolean completedNormally, List<ExecutionTraceEntry> trace){ this.completedNormally = completedNormally; this.trace = trace; }
}

class WorkflowEngine {
    private final EngineContext ctx;
    public WorkflowEngine(EngineContext ctx){ this.ctx = ctx; }

    public ExecutionResult run(Workflow wf, Map<String,Object> inputFields){
        FieldStore fields = new FieldStore();
        inputFields.forEach(fields::put);
        Map<String,Step> byLabel = wf.steps.stream().collect(Collectors.toMap(s -> s.label, s -> s));

        // start at the first step
        String curLabel = wf.steps.isEmpty() ? null : wf.steps.get(0).label;
        boolean normalEnd = true;
        List<ExecutionTraceEntry> trace = new ArrayList<>();

        try {
            while (curLabel != null) {
                Step step = byLabel.get(curLabel);
                if (step == null) throw new IllegalStateException("Unknown step label: " + curLabel);
                ActionResult res = step.action.execute(fields, ctx);
                trace.add(new ExecutionTraceEntry(step.label, step.action.name, res.success, fields.snapshot()));
                // write back any outputs
                res.outputs.forEach(fields::put);
                curLabel = res.success ? step.onSuccess : step.onFail;
            }
        } catch (Exception ex) {
            normalEnd = false;
            trace.add(new ExecutionTraceEntry(curLabel, "<exception>", false, fields.snapshot()));
            // in real engine, record exception, optionally support retry, dead-letter, etc.
        }

        return new ExecutionResult(normalEnd, trace);
    }
}

// ---- Example usage / builder helpers ----

class StepBuilder {
    private String label;
    private Action action;
    private String onSuccess;
    private String onFail;
    public StepBuilder label(String l){ this.label = l; return this; }
    public StepBuilder action(Action a){ this.action = a; return this; }
    public StepBuilder onSuccess(String l){ this.onSuccess = l; return this; }
    public StepBuilder onFail(String l){ this.onFail = l; return this; }
    public Step build(){ return new Step(label, action, onSuccess, onFail); }
}

// ---- Demo main ----

class WorkflowEngineDemo {
    public static void main(String[] args) throws Exception {
        // Build a tiny workflow:
        // 1) calc sum = a + b
        // 2) compare sum > threshold
        //   - if true -> sql action to "persist"
        //   - if false -> finish

        Action calc = new CalculateAction("Add a+b", (fields) -> {
            Number a = (Number) fields.get("a");
            Number b = (Number) fields.get("b");
            double sum = (a==null?0:a.doubleValue()) + (b==null?0:b.doubleValue());
            return Map.of("sum", sum);
        });

        Action check = new CompareAction("Check threshold", (fields, ctx) -> {
            Number sum = (Number) fields.get("sum");
            Number threshold = (Number) fields.get("threshold");
            if (threshold == null) threshold = 10;
            return sum.doubleValue() > threshold.doubleValue();
        });

        Action persist = new SqlAction("Persist sum", "INSERT INTO sums (value) VALUES ({{sum}})");

        Workflow wf = new Workflow("wf1", "AddAndMaybePersist", Arrays.asList(
                new StepBuilder().label("start").action(calc).onSuccess("check").onFail(null).build(),
                new StepBuilder().label("check").action(check).onSuccess("persist").onFail(null).build(),
                new StepBuilder().label("persist").action(persist).onSuccess(null).onFail(null).build()
        ));

        EngineContext ctx = new EngineContext(new MockDataAccess());
        WorkflowEngine engine = new WorkflowEngine(ctx);

        Map<String,Object> input = new HashMap<>();
        input.put("a", 7);
        input.put("b", 8);
        input.put("threshold", 10);

        ExecutionResult result = engine.run(wf, input);

        System.out.println("Completed normally: " + result.completedNormally);
        System.out.println("Trace:");
        AtomicInteger i = new AtomicInteger(1);
        for (ExecutionTraceEntry e : result.trace) {
            System.out.println(i.getAndIncrement() + ") step=" + e.stepLabel + " action=" + e.actionName + " success=" + e.success + " fields=" + e.fieldSnapshot);
        }
    }
}
