<%--
  Created by IntelliJ IDEA.
  User: NCri
  Date: 2015. 12. 29.
  Time: 오전 10:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="kr.ac.ssu.ikatool.protocol.AutoCompleteListProtocol" %>

<%
    String inputText = request.getParameter("text");
    String flag = request.getParameter("flag");
    out.print(AutoCompleteListProtocol.getCompleteRuleList(inputText, Integer.valueOf(flag)));
    out.flush();
%>
