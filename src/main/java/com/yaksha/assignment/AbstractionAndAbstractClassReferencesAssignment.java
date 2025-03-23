package com.yaksha.assignment;

// Abstract class Animal demonstrating abstraction with subclasses
abstract class Animal {

	// Abstract method that must be implemented by subclasses
	public abstract void sound();

	// Non-abstract method
	public void eat() {
		System.out.println("This animal eats food.");
	}
}

// Dog class that extends Animal and provides implementation for the abstract method
class Dog extends Animal {

	// Implementation of the abstract method sound
	@Override
	public void sound() {
		System.out.println("The dog barks.");
	}
}

// Cat class that extends Animal and provides implementation for the abstract method
class Cat extends Animal {

	// Implementation of the abstract method sound
	@Override
	public void sound() {
		System.out.println("The cat meows.");
	}
}

public class AbstractionAndAbstractClassReferencesAssignment {
	public static void main(String[] args) {
		// Using abstract class references to refer to objects of subclasses
		Animal myDog = new Dog();
		Animal myCat = new Cat();

		// Calling the sound() method (polymorphism in action)
		myDog.sound(); // Should print "The dog barks."
		myCat.sound(); // Should print "The cat meows."

		// Calling the non-abstract method eat() which is inherited by both subclasses
		myDog.eat(); // Should print "This animal eats food."
		myCat.eat(); // Should print "This animal eats food."
	}
}
