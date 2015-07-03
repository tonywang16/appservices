// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]


grails.config.locations = ["classpath:config.properties","classpath:weixinConfig.properties"]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j.main = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

grails.plugin.springsecurity.password.algorithm = 'SHA-256'
grails.plugin.springsecurity.password.hash.iterations = 1
grails.plugin.springsecurity.successHandler.alwaysUseDefault = false
// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.paimeilv.basic.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.paimeilv.basic.UserRole'
grails.plugin.springsecurity.authority.className = 'com.paimeilv.basic.Role'

grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/login/ajaxSuccess'
grails.plugin.springsecurity.apf.filterProcessesUrl = '/mlogin'

grails.plugin.springsecurity.logout.filterProcessesUrl = '/mlogout'


grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	'/**/**/**':['permitAll'],
	'/user/*':                           ['IS_AUTHENTICATED_FULLY'],
	 '/role/*':                           ['IS_AUTHENTICATED_FULLY'],
	 '/**/register/**':                      ['IS_AUTHENTICATED_ANONYMOUSLY'],
	 '**/user/**' : ['permitAll'],
	 '/**/conn/**': ['permitAll']
]

grails {
	mail {
		host = "smtp.exmail.qq.com"
		port = 465
		username = "service@paimeilv.com"
		password = "paimeilv2014"
		props = ["mail.smtp.auth":"true",
			   "mail.smtp.socketFactory.port":"465",
			   "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
			   "mail.smtp.socketFactory.fallback":"false"]
	}
 }

grails.plugin.springsecurity.ui.register.emailFrom="service@paimeilv.com"
grails.plugin.springsecurity.ui.register.defaultRoleNames = ['ROLE_USER']
grails.plugin.springsecurity.ui.password.minLength	= 6
grails.plugin.springsecurity.ui.password.maxLength = 64
grails.plugin.springsecurity.ui.password.validationRegex = '[a-zA-Z0-9]{6,64}$'
grails.plugin.springsecurity.useSecurityEventListener = true

grails.plugin.springsecurity.ui.identifyingCode.emailBody  = '''\
	<table  style="width:700px;text-align:left;padding:0px;margin:0px; margin:auto" align="center">
	<tr >
	  <td  style="text-align:left;padding:0px"><span><a href="http://www.paimeilv.com/" target="_blank">
	  <img style="border=0px; width=200px; height=76px;"  http://7vzrp0.com1.z0.glb.clouddn.com/email_logo.png" alt="http://7vzrp0.com1.z0.glb.clouddn.com/email_logo.png"/></a>
	  </span>
	  </td>
	</tr>
	 
	<tr style="height:60px;line-height:60px;font-size:14px">
		<td >亲爱的&nbsp;<span style="color:#ff99cc">$username</span>,你好</td></tr>
		<tr style="height:20px;line-height:20px;font-size:14px"><td ><span>欢迎入驻拍美旅！
		</td></tr>
		<tr style="height:20px;line-height:20px;font-size:14px"><td ><span>您的验证码如下， 愿您在拍美旅度过愉快的时光。</td></tr>
		<tr style="height:20px;line-height:20px;font-size:14px;color:red"><td >$param</td></tr>
	    <tr style="height:10px"><td></td></tr>
		<tr  style="height:20px;line-height:20px;font-size:14px;" ><td>此外，旅拍菌特意为你准备了新手入门攻略：<a>[快来看看吧]</a></td></tr>
    <tr style="height:10px"><td></td></tr>
	<tr ><td ><hr style="border:1px solid grep;height:0px;" /></td></tr>
	<tr style="height:22px;line-height:22px;font-size:14px;"><td  ><span
  style='color:#C5C5C5'>如果你错误地收到了此电子邮件，你可以放心忽略此封邮件，无需进行任何操作</span></td></tr>
	<tr><td><img style="border=0px; width=800px; height=100px;" src="http://paimeilv.qiniudn.com/foot_bg.jpg" alt="拍摄你的美好旅程"></td></tr>
	<tr ><td  style="text-align:center;height:22px;line-height:22px;">我们的网址：<a href="www.paimeilv.com"  target="_blank" style="text-decoration:none"><span style="color:#ff99cc">www.paimeilv.com </span></a></td>
</tr>
</table>

'''
grails.plugin.springsecurity.ui.identifyingCode.emailSubject = '拍美旅验证码'