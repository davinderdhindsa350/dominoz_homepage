<%@ page isELIgnored="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html >
    <head>
	<meta name="robots" content="noindex,follow">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/public/default/sass/app.scss?ver=081120191500">
</head>

<body>

	<div class="header-wrap clearfix logo-header fixed-header ">
		<div class="container">
			 <div class="nav-wrap">
				<div  class="jfl-logo">
						<a  href="/" class="logo-normall">
													<img  alt="Dominos header logo" src="${pageContext.request.contextPath}/public/default/images/global/jfl-logo-sm.png">
						</a>
				</div> 
			 </div>
		</div>
	</div>

    <div class="pg-wrapper full push-content-down mb30">
        <div class="container">
            <div class="page-error">
                <!-- <div class="addbox-720X90 mb20 m-auto"></div> -->
                <div class="error-row">
                    <!-- <div class="error-col-left">
                        <div class="addbox-160X600"></div>
                    </div> -->
                    <div class="error-col-middle">
                        <div class="error-sec">
                            <div class="text-center">
                                <img src="${pageContext.request.contextPath}/public/default/images/global/404.png" class="no-result mb20" />
                                <p class="no-result-text fs-18  medium">Sorry, we couldn't find the page</p>
                                <p class="color-touch-black fs-15 medium  mt15">The document you are looking for has either been moved or doesn't exists.</p>
                                <!-- <span  class="action-cta ">
                                        <span class="loader-custom"></span>
                                </span>
                                <div class="pr mt15">
                                    <p data-time="10" id="counter-message">Redirecting to Home page in 10 seconds.....</p>
                                </div>                  -->
                            </div>
                         
                            
                        </div>
                
                        
                    </div>

                </div>
            </div>
        </div>
    </div>


    <span class="hide" style="display:none">India</span>

    <style>
        .logo-header{
            width: 100%;
            height: 72px;
            background-color: #fff;
            position: relative;
            box-shadow: 0 8px 6px -6px rgba(0,0,0,.13);
        }
        
        .page-error {
            background: #fff;
            padding: 20px;
            margin-top: 50px; 
        }
        .error-row:after {
            content: "";
            display: table;
            clear: both; 
        }
        .error-row .error-col-left {
            float: left;
            width: 160px;
            height: auto;
        }
        .error-row .error-col-right {
            float: left;
            width: 160px;
            height: auto;
        }
        .error-row .error-col-middle {
            float: none;
            width: calc(100% - 320px); 
            margin:0 auto;
        }
        .error-box {
            width: 100%;
            padding: 30px 15px 0px 30px;
            background: #fff; 
        }
        .error-box:after {
            content: "";
            display: block;
            clear: both; 
        }
        .error-box .error-imgbox {
            float: left;
            width: 420px; 
        }
        .error-box .error-imgbox img {
            width: 100%;
            height: auto; 
        }
        .error-box .error-content {
            float: left;
            width: calc(100% - 340px);
            padding-top: 20px;
            margin-top: 10px;
            margin-left: -80px; 
            position: relative;
            text-align: center;
        }
        .error-box .error-content h2 {
            font-size: 2.857em;
            color: #000; 
        }
        .error-box .error-content p {
            margin-top: 20px;
            color: #9ea4b8;
            font-family: "poppins-regular-webfont";
            font-size: 1.44em;
            line-height: 34px; 
        }
        .addbox-160X600 {
            width: 160px;
            height: 600px;
            background: #fff; 
        }
        .addbox-720X90 {
            width: 720px;
            height: 90px;
            background: #fff; 
        }
        .text-center {
            text-align: center; 
        }
        .pTB10 {
            padding-top: 10px !important;
            padding-bottom: 10px !important; 
        }
        .mt15 {
            margin-top: 15px !important; 
        }
        .mt20 {
            margin-top: 20px !important; 
        }
        .mb20 {
            margin-bottom: 20px; 
        }
        .ib {
            display: inline-block; 
        }
        .wt-120 {
            width: 120px !important; 
        }
        .clearit {
            zoom: 1; 
        }
        .clearit:after {
            content: "";
            display: table; 
        }
        .clearit:after {
            clear: both; 
        }
        .m-auto {
            margin-left: auto !important;
            margin-right: auto !important; 
        }
        .btn:hover, .btn.active {
            opacity: 1;
            background: #4a2189;
            border: 1px solid #5D4DA8;
            color: #fff;
        }
        .btn{
            padding: 16px 18px;
            font-size: 1.071em;
            line-height: normal;
            cursor: pointer;
            border: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
            background: #5D4DA8;
            border: 1px solid #5D4DA8;
            color: #fff;
            border-radius: 0px 0px 0px 0px;
            -webkit-border-radius: 0px 0px 0px 0px;
            -moz-border-radius: 0px 0px 0px 0px;
        }
     
        .action-cta {
            margin:20px auto;
            display: block;
            text-align: center;
            width: 60px;
            position: relative;
            top: 0;
            z-index: 1;
            background: rgba(175, 175, 175, 0.6);
            height: 60px;
            border-radius: 50%;
         }
        .loader-custom {
            height: 0;
            width: 0;
            padding: 15px;
            border: 1px solid #666;
            border-right-color: #ccc;
            border-radius: 22px;
            -webkit-animation: rotate 1s infinite linear;
            position: absolute;
            left: 50%;
            top: 50%;
        }
        .action-cta .loader-custom {
            padding: 10px;
            right: 33%;
            left: auto;
            top: 32%;
        }

        .no-result {
            width: 290px;
            max-width: 100%;
            height: auto;
            display: block;
            margin: 20px auto;
        }
        /* @include my-screen(small) {
                width: 190px;
            } */

        .no-result-text{
        color:#1D1E1F;
        }
        .try-another-phase{
        padding: 15px 25px;
        font-size: 16px;
        cursor: pointer;
        border: none;
        -webkit-appearance: none;
        -moz-appearance: none;
        appearance: none;
        background: #5c4aae;
        color: #ffffff;
        width: 160px;
        border-radius:4px;
        }
        .try-another-phase:hover{
            background: #6c54da;
        }
        /* .error-page{
        background:#ffffff;
        } */
        .error-sec{
            max-width: 600px;
            margin: 0 auto;
        }
        .error-sec:after {
            content: "";
            display: table;
            clear: both; 
        }
        .error-sec .right-redirection{
            background:#F5F7F9;
            padding:10px 50px 20px;
            margin: 15px 0;
                
        }

        .error-sec .btn-link{
            color: #5B4AAC;
            text-decoration: underline;
            margin: 10px 0;
            display: block;
            text-align: left;
        }



        @-webkit-keyframes rotate {
            0% { -webkit-transform: rotate(0deg); }
            100% { -webkit-transform: rotate(360deg); }
            }

            @keyframes rotate {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
            }
        @media only screen and (max-width: 1188px) {
            .error-box {
                padding: 30px 15px 0px 15px; 
            }
            .error-box .error-imgbox {
                width: 360px; 
            }
            .error-box .error-content {
                width: calc(100% - 280px); 
            }
            .error-box .error-content p {
                line-height: 28px;
                font-size: 1.143em;
                margin-top: 15px; 
            }
        }
        @media only screen and (max-width: 990px) {
            .header-wrap{
                background:#fff;
            }
            .nav-wrap {
                min-height: 65px;
            }
            .jfl-logo{
                display:block;
            }
            .error-row .error-col-left{
                width:100%;
            }
            .addbox-160X600 {
                width:100%;
                height:200px;
                background: #fff;
            }
           
            .error-row .error-col-middle {
                float: left;
                width: 100%;
            }
            .error-box {
                padding: 15px; 
            }
            .error-box .error-imgbox {
                float: none;
                margin: 0 auto;
                width: 100%;
                max-width: 100%; 
            }
            .error-box .error-content {
                width: 100%;
                margin-left: 0px;
                padding-top: 0;
                margin-top: -50px; 
            }
            
        }
        @media only screen and (max-width: 768px) {
            .error-box .error-content {
                margin-top: -20px; 
            }
            .error-box .error-content h2 {
                font-size: 1.714em; 
            }
            .error-sec .right-redirection{
                padding:10px 15px 20px;
            }
        }
    </style>
    <script type="text/javascript">

    </script>
    
</body>
</html>

