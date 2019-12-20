//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.12.18 at 11:10:15 PM CET 
//


package com.ftn.scientific_papers.model.scientific_paper;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for TSimpleParagraph complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TSimpleParagraph"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;group ref="{https://github.com/AnaMijailovic/XML-Tim19}simple_paragraph_elements"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSimpleParagraph", propOrder = {
    "content"
})
public class TSimpleParagraph {

    @XmlElementRefs({
        @XmlElementRef(name = "b", namespace = "https://github.com/AnaMijailovic/XML-Tim19", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "i", namespace = "https://github.com/AnaMijailovic/XML-Tim19", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "u", namespace = "https://github.com/AnaMijailovic/XML-Tim19", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "unordered_list", namespace = "https://github.com/AnaMijailovic/XML-Tim19", type = UnorderedList.class, required = false),
        @XmlElementRef(name = "ordered_list", namespace = "https://github.com/AnaMijailovic/XML-Tim19", type = OrderedList.class, required = false),
        @XmlElementRef(name = "quote", namespace = "https://github.com/AnaMijailovic/XML-Tim19", type = Quote.class, required = false)
    })
    @XmlMixed
    protected List<Object> content;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * {@link JAXBElement }{@code <}{@link TEmphasis }{@code >}
     * {@link UnorderedList }
     * {@link OrderedList }
     * {@link Quote }
     * {@link String }
     * 
     * 
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}