<%--
  Created by IntelliJ IDEA.
  User: NCri
  Date: 2015. 12. 23.
  Time: 오후 10:31
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="kr.ac.ssu.ikatool.protocol.PatientListProtocol"%>

<%
    String patientID = request.getParameter("PatientID");
    out.print(PatientListProtocol.getPatientList(patientID));
    out.flush();
%>