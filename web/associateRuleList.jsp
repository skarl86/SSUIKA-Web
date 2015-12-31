<%@ page import="kr.ac.ssu.ikatool.protocol.AssociateRuleListProtocol" %><%--
  Created by IntelliJ IDEA.
  User: NCri
  Date: 2015. 12. 31.
  Time: 오후 3:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ant = request.getParameter("antecedents");
    String cons = request.getParameter("consequents");
    out.print(AssociateRuleListProtocol.getAssoicateRule(ant, cons));
    out.flush();
%>
