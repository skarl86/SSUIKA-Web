<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015-12-29
  Time: 오후 4:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="kr.ac.ssu.ikatool.protocol.SetRuleProtocol" %>

<%
    if(request.getParameter("patientID") != null && request.getParameter("opinionID") != null && request.getParameter("antecedents")  != null&& request.getParameter("consequents") != null && request.getParameter("authorID") != null){
        String patientID = request.getParameter("patientID");
        String opinionID = request.getParameter("opinionID");
        String antecedents = request.getParameter("antecedents");
        String consequents = request.getParameter("consequents");
        String authorID = request.getParameter("authorID");
        out.print(SetRuleProtocol.insertRule(patientID,opinionID,antecedents,consequents, authorID));
        out.flush();
    }else {
        out.print(SetRuleProtocol.getJSON("check parameter","-1"));
        out.flush();
    }


%>
