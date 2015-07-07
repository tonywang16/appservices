import com.paimeilv.QiNiuService
import com.paimeilv.basic.Role
import com.paimeilv.basic.User
import com.paimeilv.basic.UserRole
import com.paimeilv.config.QiniuConfig

class BootStrap {

    def init = { servletContext ->
		
		QiniuConfig qiniuconfig =  QiniuConfig.first()
		if(qiniuconfig){
			QiNiuService.ACCESS_KEY = qiniuconfig.accesskey
			QiNiuService.SECRET_KEY = qiniuconfig.secretkey
			QiNiuService.UPLOAD_BUCKET = qiniuconfig.uploadbucket
			QiNiuService.TEMP_BUCKET = qiniuconfig.tempbucket
		}
		def adminUser = User.findByUsername("admin")
		def adminRole = Role.findByAuthority("ROLE_ADMIN") ?: new Role(authority: 'ROLE_ADMIN').save()
		def userRole = Role.findByAuthority("ROLE_USER") ?: new Role(authority: 'ROLE_USER').save() //普通注册用户权限
		
		if (!adminUser) {
			adminUser = new User(
					username: 'admin',
					password: "password",
					email:"admin@xmmoyi.com",
					enabled: true)
			adminUser.save(flush:true)
			UserRole.create(adminUser, adminRole)
		}
    }
    def destroy = {
    }
}
