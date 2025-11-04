package com.qulron.qulron_workflow_engine.actions;

import com.qulron.qulron_workflow_engine.engine.Action;
import com.qulron.qulron_workflow_engine.engine.ExecutionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("DatabaseAction")
public class DatabaseAction implements Action {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseAction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean execute(ExecutionContext context) {
        String procName = (String) context.getVar("procedure");
        jdbcTemplate.execute("EXEC " + procName);
        return true;
    }
}
