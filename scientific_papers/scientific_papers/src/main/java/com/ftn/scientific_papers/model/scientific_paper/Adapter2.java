//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.1 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.12.18 at 11:10:15 PM CET 
//


package com.ftn.scientific_papers.model.scientific_paper;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter2
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (com.ftn.scientific_papers.util.MyDatatypeConverter.parseYear(value));
    }

    public String marshal(Date value) {
        return (com.ftn.scientific_papers.util.MyDatatypeConverter.printYear(value));
    }

}
