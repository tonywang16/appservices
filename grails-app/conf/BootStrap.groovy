import com.paimeilv.basic.Role
import com.paimeilv.basic.User
import com.paimeilv.basic.UserOpenID
import com.paimeilv.basic.UserRole

class BootStrap {

    def init = { servletContext ->
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
