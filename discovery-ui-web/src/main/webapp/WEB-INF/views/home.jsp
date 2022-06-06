<%@ page isELIgnored="false" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="${htmlLang}" dir="${htmlLang == 'ar' ? 'rtl' : 'ltr'}" >

<head>
<title>${htmlHeadTitle}</title>
 <meta name="Canonical" content="${htmlHeadMeta.metaCanonical}"> 
 <meta name="Robots" content="${htmlHeadMeta.metaRobots}"> 
 <meta name="Keywords" content="${htmlHeadMeta.metaKeywords}" /> 
 <meta name="Description" content="${htmlHeadMeta.metaDescription}" />   
 <meta name="Title" content="${htmlHeadMeta.metaTitle}" /> 
 <link rel="${htmlHeadLinks.rel}" href="${htmlHeadLinks.href}">
<c:forEach items="${htmlHeadHeaderContent}" var="content">
            ${content.html}
        </c:forEach>
<!-- Setting Site Context -->
<script>
    var version =  "${version}";
	var _siteProps_ = {
		name : "${siteProps.site}",	
		platform : "${siteProps.platform}",
		platform_id : "${siteProps.platformId}",
		category_id : "${siteProps.categoryId}",
		category_name : "${siteProps.category}",
		language : "${siteProps.lang}",
		domain: "${siteProps.domain}",
		props: ${siteProps.properties},
		under_maintainance: "${siteProps.underMaintainance}",
		siteContext: "${siteProps.context}",
		favicon: "${siteProps.favicon}",
		profile: "${siteProps.profile}"
	};
</script>

</head>
<body class="${htmlBodyBodyClass}">
	<div id="pageLoader">
		<div class="img">
			<div class="jfl-loader-container">
			<div class="jfl-loader"></div>
			</div>
		</div>
	</div>
	${htmlBodyContent}

	<c:forEach items="${htmlBodyFooterFooterContent}" var="content">
        ${content.html}
    </c:forEach>
</body>
</html>
