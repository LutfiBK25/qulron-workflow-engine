package com.qulron.qulron_workflow_engine.utility;

import com.qulron.qulron_workflow_engine.enums.ActionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ActionTypeConverter implements AttributeConverter<ActionType, Integer> {


    @Override
    public Integer convertToDatabaseColumn(ActionType actionType) {
        if(actionType == null){
            return null;
        }
        return actionType.getAction(); // returns and int
    }

    @Override
    public ActionType convertToEntityAttribute(Integer i) {
            if(i == null){
                return null;
            }
            for (ActionType actionType : ActionType.values()){
                if(actionType.getAction() == i){
                    return actionType;
                }
            } throw new IllegalArgumentException("Unknown status value: " + i);
    }
}
