// Place your Spring DSL code here
beans = {
	userDetailsService(com.paimeilv.CustomUserDetailsService)
	authenticationProcessingFilter(com.paimeilv.filters.DisUsernamePasswordAuthenticationFilter) {
		authenticationManager = ref('authenticationManager')
		sessionAuthenticationStrategy = ref('sessionAuthenticationStrategy')
		authenticationSuccessHandler = ref('authenticationSuccessHandler')
		authenticationFailureHandler = ref('authenticationFailureHandler')
		rememberMeServices = ref('rememberMeServices')
		authenticationDetailsSource = ref('authenticationDetailsSource')
	}
}
