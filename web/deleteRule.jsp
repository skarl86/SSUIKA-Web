<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-12-31
  Time: 오후 7:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="kr.ac.ssu.ikatool.protocol.DeleteRuleProtocol" %>

<%
    if(request.getParameter("patientID") != null && request.getParameter("opinionID") != null && request.getParameter("ruleID")  != null){
        String patientID = request.getParameter("patientID");
        String opinionID = request.getParameter("opinionID");
        String ruleID = request.getParameter("ruleID");

        out.print(DeleteRuleProtocol.deleteRule(patientID,opinionID,ruleID));
        out.flush();
    }else {
        out.print(DeleteRuleProtocol.getJSON("check parameter","-1"));
        out.flush();
    }


%>