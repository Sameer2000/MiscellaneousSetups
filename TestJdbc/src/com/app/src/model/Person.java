package com.app.src.model;

public class Person {

	private String name;

	private String amount;

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", amount=" + amount + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}
