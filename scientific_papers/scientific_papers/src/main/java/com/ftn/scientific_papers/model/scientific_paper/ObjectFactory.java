//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.12.18 at 11:10:15 PM CET 
//


package com.ftn.scientific_papers.model.scientific_paper;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ftn.scientific_papers.model.scientific_paper package. 
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

    private final static QName _Heading_QNAME = new QName("https://github.com/AnaMijailovic/XML-Tim19", "heading");
    private final static QName _TEmphasisB_QNAME = new QName("https://github.com/AnaMijailovic/XML-Tim19", "b");
    private final static QName _TEmphasisI_QNAME = new QName("https://github.com/AnaMijailovic/XML-Tim19", "i");
    private final static QName _TEmphasisU_QNAME = new QName("https://github.com/AnaMijailovic/XML-Tim19", "u");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ftn.scientific_papers.model.scientific_paper
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ScientificPaper }
     * 
     */
    public ScientificPaper createScientificPaper() {
        return new ScientificPaper();
    }

    /**
     * Create an instance of {@link Table }
     * 
     */
    public Table createTable() {
        return new Table();
    }

    /**
     * Create an instance of {@link ScientificPaper.Body }
     * 
     */
    public ScientificPaper.Body createScientificPaperBody() {
        return new ScientificPaper.Body();
    }

    /**
     * Create an instance of {@link ScientificPaper.Head }
     * 
     */
    public ScientificPaper.Head createScientificPaperHead() {
        return new ScientificPaper.Head();
    }

    /**
     * Create an instance of {@link Affiliation }
     * 
     */
    public Affiliation createAffiliation() {
        return new Affiliation();
    }

    /**
     * Create an instance of {@link Author }
     * 
     */
    public Author createAuthor() {
        return new Author();
    }

    /**
     * Create an instance of {@link TPerson }
     * 
     */
    public TPerson createTPerson() {
        return new TPerson();
    }

    /**
     * Create an instance of {@link Chapter }
     * 
     */
    public Chapter createChapter() {
        return new Chapter();
    }

    /**
     * Create an instance of {@link TComplexParagraph }
     * 
     */
    public TComplexParagraph createTComplexParagraph() {
        return new TComplexParagraph();
    }

    /**
     * Create an instance of {@link Image }
     * 
     */
    public Image createImage() {
        return new Image();
    }

    /**
     * Create an instance of {@link UnorderedList }
     * 
     */
    public UnorderedList createUnorderedList() {
        return new UnorderedList();
    }

    /**
     * Create an instance of {@link OrderedList }
     * 
     */
    public OrderedList createOrderedList() {
        return new OrderedList();
    }

    /**
     * Create an instance of {@link Table.Tr }
     * 
     */
    public Table.Tr createTableTr() {
        return new Table.Tr();
    }

    /**
     * Create an instance of {@link Quote }
     * 
     */
    public Quote createQuote() {
        return new Quote();
    }

    /**
     * Create an instance of {@link Reference }
     * 
     */
    public Reference createReference() {
        return new Reference();
    }

    /**
     * Create an instance of {@link TSimpleParagraph }
     * 
     */
    public TSimpleParagraph createTSimpleParagraph() {
        return new TSimpleParagraph();
    }

    /**
     * Create an instance of {@link TEmphasis }
     * 
     */
    public TEmphasis createTEmphasis() {
        return new TEmphasis();
    }

    /**
     * Create an instance of {@link ScientificPaper.Body.Abstract }
     * 
     */
    public ScientificPaper.Body.Abstract createScientificPaperBodyAbstract() {
        return new ScientificPaper.Body.Abstract();
    }

    /**
     * Create an instance of {@link ScientificPaper.Body.References }
     * 
     */
    public ScientificPaper.Body.References createScientificPaperBodyReferences() {
        return new ScientificPaper.Body.References();
    }

    /**
     * Create an instance of {@link ScientificPaper.Head.Title }
     * 
     */
    public ScientificPaper.Head.Title createScientificPaperHeadTitle() {
        return new ScientificPaper.Head.Title();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "heading")
    public JAXBElement<String> createHeading(String value) {
        return new JAXBElement<String>(_Heading_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "b", scope = TEmphasis.class)
    public JAXBElement<TEmphasis> createTEmphasisB(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisB_QNAME, TEmphasis.class, TEmphasis.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "i", scope = TEmphasis.class)
    public JAXBElement<TEmphasis> createTEmphasisI(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisI_QNAME, TEmphasis.class, TEmphasis.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "u", scope = TEmphasis.class)
    public JAXBElement<TEmphasis> createTEmphasisU(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisU_QNAME, TEmphasis.class, TEmphasis.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "b", scope = TSimpleParagraph.class)
    public JAXBElement<TEmphasis> createTSimpleParagraphB(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisB_QNAME, TEmphasis.class, TSimpleParagraph.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "i", scope = TSimpleParagraph.class)
    public JAXBElement<TEmphasis> createTSimpleParagraphI(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisI_QNAME, TEmphasis.class, TSimpleParagraph.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "u", scope = TSimpleParagraph.class)
    public JAXBElement<TEmphasis> createTSimpleParagraphU(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisU_QNAME, TEmphasis.class, TSimpleParagraph.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "b", scope = TComplexParagraph.class)
    public JAXBElement<TEmphasis> createTComplexParagraphB(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisB_QNAME, TEmphasis.class, TComplexParagraph.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "i", scope = TComplexParagraph.class)
    public JAXBElement<TEmphasis> createTComplexParagraphI(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisI_QNAME, TEmphasis.class, TComplexParagraph.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     */
    @XmlElementDecl(namespace = "https://github.com/AnaMijailovic/XML-Tim19", name = "u", scope = TComplexParagraph.class)
    public JAXBElement<TEmphasis> createTComplexParagraphU(TEmphasis value) {
        return new JAXBElement<TEmphasis>(_TEmphasisU_QNAME, TEmphasis.class, TComplexParagraph.class, value);
    }

}
