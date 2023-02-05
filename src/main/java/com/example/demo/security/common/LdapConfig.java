package com.example.demo.security.common;

public class LdapConfig {
	private LdapConfig() {
		//do nothing
	}

	public static final String ldapUrls = "ldap.urls";
	public static final String ldapBaseDn = "ldap.base.dn";
	public static final String ldapUserName = "ldap.username";
	public static final String ldapPassword = "ldap.password";
	public static final String ldapUserDnPattern = "ldap.user.dn.pattern";
}
