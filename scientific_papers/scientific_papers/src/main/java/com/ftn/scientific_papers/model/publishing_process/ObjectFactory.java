//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.16 at 08:14:40 PM CEST 
//


package com.ftn.scientific_papers.model.publishing_process;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ftn.scientific_papers.model.publishing_process package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ftn.scientific_papers.model.publishing_process
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PublishingProcess }
     * 
     */
    public PublishingProcess createPublishingProcess() {
        return new PublishingProcess();
    }

    /**
     * Create an instance of {@link PublishingProcess.PaperVersion }
     * 
     */
    public PublishingProcess.PaperVersion createPublishingProcessPaperVersion() {
        return new PublishingProcess.PaperVersion();
    }

    /**
     * Create an instance of {@link VersionReview }
     * 
     */
    public VersionReview createVersionReview() {
        return new VersionReview();
    }

    /**
     * Create an instance of {@link PublishingProcess.PaperVersion.VersionReviews }
     * 
     */
    public PublishingProcess.PaperVersion.VersionReviews createPublishingProcessPaperVersionVersionReviews() {
        return new PublishingProcess.PaperVersion.VersionReviews();
    }

}
