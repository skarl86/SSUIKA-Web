<%@ page import="kr.ac.ssu.ikatool.protocol.getAtomValueProtocol" %>
<%@ page import="kr.ac.ssu.ikatool.protocol.SetRuleProtocol" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016-01-04
  Time: 오후 8:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if(request.getParameter("atomID") != null){
        String atomID = request.getParameter("atomID");

        out.print(getAtomValueProtocol.getAtomValue(atomID));
        out.flush();
    }else {
        out.print(SetRuleProtocol.getJSON("check parameter","-1"));
        out.flush();
    }
%>