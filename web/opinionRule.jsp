<%@ page import="kr.ac.ssu.ikatool.protocol.OpinionRelatedRuleProtocol" %><%--
  Created by IntelliJ IDEA.
  User: NCri
  Date: 2015. 12. 30.
  Time: 오전 11:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String patientID = request.getParameter("PatientID");
    String opinionID = request.getParameter("OpinionID");
    out.print(OpinionRelatedRuleProtocol.getRuleList(patientID, opinionID));
    out.flush();
%>