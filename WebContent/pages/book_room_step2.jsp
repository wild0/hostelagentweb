<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  import="java.util.*,tw.com.orangice.sf.erp.manager.*,tw.com.orangice.sf.erp.model.order_model.impl.*,tw.com.wild0.hostelagentweb.controller.*" %>
<%
ServletContext sc = request.getServletContext();
//String webAppPath = sc.getRealPath("/");



ArrayList<OrderDetailModelImpl> data = new ArrayList<OrderDetailModelImpl>();
try{
	
}
catch(Exception e){
	e.printStackTrace();
}
%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SB Admin 2 - Bootstrap Admin Theme</title>

    <!-- Bootstrap Core CSS -->
    <link href="../bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="../bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="../dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="../bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	
	<!-- Datepicker -->
	<link src="../datepicker/css/bootstrap-datepicker3.css"  rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.html">SB Admin v2.0</a>
            </div>
            <!-- /.navbar-header -->

            <!-- /.navbar-header -->
			<%@ include file="./nav/nav.jsp" %>
            
            <!-- /.navbar-top-links -->

            <%@ include file="./nav/menu.jsp" %>
            
            
            <!-- /.navbar-static-side -->
            <!-- /.navbar-static-side -->
        </nav>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Employee Registration</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Basic Form Elements
                        </div>
                        <div class="panel-body">
                            <div class="dataTable_wrapper">
                                <table class="table table-striped table-bordered table-hover" id="dataTables-example">
                                    <thead>
                                        <tr>
                                            <th>Customer Uname</th>
                                            <th>Customer Email</th>
                                            <th>Customer Birth</th>
                                            <th>Operation</th>
                                           
                                        </tr>
                                    </thead>
                                    <tbody>
                                    
                                    <%
									for(int i=0;i<data.size();i++){
										OrderDetailModelImpl orderDetail = data.get(i);
										
										out.println("<tr class=\"odd gradeX\">");
										//out.println("<td>"+String.valueOf(employee.getId()) +"</td>");
										//out.println("<td>"+String.valueOf(employee.getUname())+"</td>");
										//out.println("<td>"+String.valueOf(employee.getRealname())+"</td>");
										out.println("<td></td>");
										out.println("</tr>");
									}
									%>
                                    
                                       
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="../bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="../bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="../bower_components/metisMenu/dist/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="../dist/js/sb-admin-2.js"></script>
    
    <!-- Datepicker -->
	<script src="../datepicker/js/bootstrap-datepicker.js"></script>
    <script>
    $(document).ready(function() {
    	$('.input-daterange').datepicker({
    	});
    });
    </script>

</body>

</html>