/**
 */
package de.tudresden.slr.model.bibtex;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see de.tudresden.slr.model.bibtex.BibtexPackage
 * @generated
 */
public interface BibtexFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	BibtexFactory eINSTANCE = de.tudresden.slr.model.bibtex.impl.BibtexFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Document</em>'. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Document</em>'.
	 * @generated
	 */
	Document createDocument();

	/**
	 * Returns a new object of class '<em>File</em>'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return a new object of class '<em>File</em>'.
	 * @generated
	 */
	BibtexFile createBibtexFile();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	BibtexPackage getBibtexPackage();

} // BibtexFactory
