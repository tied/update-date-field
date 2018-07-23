package com.eid.plugins.jira.workflow;

import java.util.Map;

import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
//import com.atlassian.jira.util.JiraUtils;
import com.atlassian.jira.issue.util.IssueChangeHolder;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.option.Option;
import com.atlassian.jira.util.ImportUtils;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the post-function class that gets executed at the end of the transition.
 * Any parameters that were saved in your factory class will be available in the transientVars Map.
 */
public class UpdateFieldPostFunction extends AbstractJiraFunctionProvider
{
    private static final Logger log = LoggerFactory.getLogger(UpdateFieldPostFunction.class);
    public static final String FIELD_MESSAGE = "customField";
 
    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException
    {
        MutableIssue issue = getIssue(transientVars);
        String custFieldName = (String)args.get(FIELD_MESSAGE);
        if (custFieldName != null) {
            CustomFieldManager customFieldManager = ComponentAccessor.getCustomFieldManager();
            CustomField cf = customFieldManager.getCustomFieldObjectByName(custFieldName);
            String valueField = (String)issue.getCustomFieldValue(cf);
            log.error( "Customfield " + custFieldName + " found with value : " + valueField);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            IssueChangeHolder changeHolder = new DefaultIssueChangeHolder();
    
            if (null == valueField || valueField.equals( "01/01/0101") ) {
                cf.updateValue(null, issue, new ModifiedValue(valueField, new SimpleDateFormat("dd/MM/yyyy").format(timestamp)),changeHolder);
            }
        }
        else {
            log.error(
            "No customfield " + custFieldName + " found for : " + issue.getKey());
        }

    }
}