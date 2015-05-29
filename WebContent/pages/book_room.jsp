<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
                    <h1 class="page-header">Room Booking</h1>
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
                            <div class="row">
                                <div class="col-lg-6">
                                    <form role="form" method="post" action="../order_manager/create_order">
                                    	
                                    	<input type="hidden" name="employee_code" value="a6ddd75081178f0ca0b3e2b640846227"/>
                                    	<input type="hidden" name ="hostel_code" value="dfb17ea6468e3ff7ac03d639ee9c3bac"/>
                                        <div class="form-group">
                                            <label>Customer</label>
                                            <input class="form-control" type="text" name="customer_name">
                                            <p class="help-block">Example block-level help text here.</p>
                                        </div>
                                        <div class="form-group">
                                            <label>Customer Identity</label>
                                            <input class="form-control" type="text" name="customer_identity">
                                            <p class="help-block">Example block-level help text here.</p>
                                        </div>
                                        <div class="form-group">
                                            <label>Customer Email</label>
                                            <input class="form-control" type="text" name="customer_email">
                                            <p class="help-block">Example block-level help text here.</p>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label>Mobile</label>
                                            <input class="form-control" type="text" name="customer_mobile">
                                            <p class="help-block">Example block-level help text here.</p>
                                        </div>
                                        <div class="form-group">
                                            <label>Person</label>
                                            <input class="form-control" type="text" name="person_count">
                                            <p class="help-block">Example block-level help text here.</p>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label>Comment</label>
                                            <textarea class="form-control" rows="3" name="reservation_comment"></textarea>
                                        </div>
                                        <div class="input-daterange input-group" id="datepicker">
    										<input type="text" class="input-sm form-control" name="reservation_start_time" />
    										<span class="input-group-addon">to</span>
    										<input type="text" class="input-sm form-control" name="reservation_end_time" />
										</div>
                                        
                                        <div class="form-group">
                                            <label>Inline Checkboxes</label>
                                            <label class="checkbox-inline">
                                                <input type="checkbox">1
                                            </label>
                                            <label class="checkbox-inline">
                                                <input type="checkbox">2
                                            </label>
                                            <label class="checkbox-inline">
                                                <input type="checkbox">3
                                            </label>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label>Inline Radio Buttons</label>
                                            <label class="radio-inline">
                                                <input type="radio" name="space_arrange" id="optionsRadiosInline1" value="automatically" checked>自動排房間
                                            </label>
                                            <label class="radio-inline">
                                                <input type="radio" name="space_arrange" id="optionsRadiosInline2" value="manually">手動排房間
                                            </label>
                   
                                        </div>
                                        <div class="form-group">
                                            <label>Selects</label>
                                            <select class="form-control">
                                                <option>1</option>
                                                <option>2</option>
                                                <option>3</option>
                                                <option>4</option>
                                                <option>5</option>
                                            </select>
                                        </div>
                                        
                                        <button type="submit" class="btn btn-default">Submit Button</button>
                                        <button type="reset" class="btn btn-default">Reset Button</button>
                                    </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                                
                            </div>
                            <!-- /.row (nested) -->
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