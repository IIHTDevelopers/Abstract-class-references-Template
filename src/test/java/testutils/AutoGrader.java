package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

public class AutoGrader {

	// Test if the code demonstrates proper abstraction with subclasses and abstract
	// class references
	public boolean testAbstractionAndAbstractClassReferences(String filePath) throws IOException {
		System.out.println("Starting testAbstractionAndAbstractClassReferences with file: " + filePath);

		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;
		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		System.out.println("Parsed the Java file successfully.");

		// Use AtomicBoolean to allow modifications inside lambda expressions
		AtomicBoolean animalClassFound = new AtomicBoolean(false);
		AtomicBoolean dogClassFound = new AtomicBoolean(false);
		AtomicBoolean catClassFound = new AtomicBoolean(false);
		AtomicBoolean dogExtendsAnimal = new AtomicBoolean(false); // Check if Dog extends Animal
		AtomicBoolean catExtendsAnimal = new AtomicBoolean(false); // Check if Cat extends Animal
		AtomicBoolean abstractSoundMethodFound = new AtomicBoolean(false);
		AtomicBoolean abstractClassReferenceUsed = new AtomicBoolean(false);
		AtomicBoolean eatMethodFound = new AtomicBoolean(false);

		// Check for class implementation, abstract methods, and references
		System.out.println("------ Class and Method Check ------");
		for (TypeDeclaration<?> typeDecl : cu.findAll(TypeDeclaration.class)) {
			if (typeDecl instanceof ClassOrInterfaceDeclaration) {
				ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) typeDecl;

				if (classDecl.getNameAsString().equals("Animal")) {
					System.out.println("Class 'Animal' found.");
					animalClassFound.set(true);
					// Check for abstract method sound
					classDecl.getMethods().forEach(method -> {
						if (method.getNameAsString().equals("sound") && method.isAbstract()) {
							abstractSoundMethodFound.set(true);
							System.out.println("Abstract method 'sound' found in 'Animal' class.");
						}
						if (method.getNameAsString().equals("eat") && !method.isAbstract()) {
							eatMethodFound.set(true);
							System.out.println("Non-abstract method 'eat' found in 'Animal' class.");
						}
					});
				}

				if (classDecl.getNameAsString().equals("Dog")) {
					System.out.println("Class 'Dog' found.");
					dogClassFound.set(true);
					// Check if Dog extends Animal
					if (classDecl.getExtendedTypes().stream().anyMatch(ext -> ext.getNameAsString().equals("Animal"))) {
						dogExtendsAnimal.set(true);
						System.out.println("Class 'Dog' extends 'Animal'.");
					} else {
						System.out.println("Error: 'Dog' does not extend 'Animal'.");
					}
				}

				if (classDecl.getNameAsString().equals("Cat")) {
					System.out.println("Class 'Cat' found.");
					catClassFound.set(true);
					// Check if Cat extends Animal
					if (classDecl.getExtendedTypes().stream().anyMatch(ext -> ext.getNameAsString().equals("Animal"))) {
						catExtendsAnimal.set(true);
						System.out.println("Class 'Cat' extends 'Animal'.");
					} else {
						System.out.println("Error: 'Cat' does not extend 'Animal'.");
					}
				}
			}
		}

		// Ensure all required classes and methods are found
		if (!animalClassFound.get() || !dogClassFound.get() || !catClassFound.get()) {
			System.out.println("Error: Class 'Animal', 'Dog', or 'Cat' not found.");
			return false;
		}

		if (!abstractSoundMethodFound.get()) {
			System.out.println("Error: Abstract method 'sound' not found in 'Animal' class.");
			return false;
		}

		if (!eatMethodFound.get()) {
			System.out.println("Error: Non-abstract method 'eat' not found in 'Animal' class.");
			return false;
		}

		// Ensure Dog and Cat extend Animal
		if (!dogExtendsAnimal.get()) {
			System.out.println("Error: 'Dog' class must extend 'Animal'.");
			return false;
		}

		if (!catExtendsAnimal.get()) {
			System.out.println("Error: 'Cat' class must extend 'Animal'.");
			return false;
		}

		// Check for abstract class references
		System.out.println("------ Abstract Class Reference Check ------");

		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			if (method.getNameAsString().equals("main")) {
				if (method.getBody().isPresent()) {
					method.getBody().get().findAll(MethodCallExpr.class).forEach(callExpr -> {
						if (callExpr.getNameAsString().equals("sound")) {
							abstractClassReferenceUsed.set(true);
							System.out.println("Abstract class reference 'Animal' used to call method 'sound'.");
						}
					});
				}
			}
		}

		// Ensure abstract class references are used correctly
		if (!abstractClassReferenceUsed.get()) {
			System.out.println("Error: Abstract class reference 'Animal' not used to call 'sound' method.");
			return false;
		}

		// If all checks pass
		System.out.println(
				"Test passed: Abstraction with subclasses and abstract class references are correctly implemented.");
		return true;
	}
}
