package com.paimeilv.basic

class Role implements Serializable {

	private static final long serialVersionUID = 1

	String authority
	String value

	Role(String authority) {
		this()
		this.authority = authority
	}
	
	Role(String authority,String value) {
		this()
		this.authority = authority
		this.value = value
	}

	@Override
	int hashCode() {
		authority?.hashCode() ?: 0
	}

	@Override
	boolean equals(other) {
		is(other) || (other instanceof Role && other.authority == authority)
	}

	@Override
	String toString() {
		authority
	}

	static constraints = {
		authority blank: false, unique: true
		value nullable: true
	}

	static mapping = {
		cache true
	}
}
