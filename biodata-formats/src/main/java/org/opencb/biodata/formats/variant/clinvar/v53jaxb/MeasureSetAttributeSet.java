//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.03.23 at 10:30:42 AM GMT 
//


package org.opencb.biodata.formats.variant.clinvar.v53jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MeasureSetAttributeSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MeasureSetAttributeSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Attribute">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;>AttributeType">
 *                 &lt;attribute name="Type" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Description"/>
 *                       &lt;enumeration value="MolecularConsequence"/>
 *                       &lt;enumeration value="HGVS"/>
 *                       &lt;enumeration value="HGVS, genomic, top level"/>
 *                       &lt;enumeration value="HGVS, genomic, top level, previous"/>
 *                       &lt;enumeration value="HGVS, genomic, top level, other"/>
 *                       &lt;enumeration value="HGVS, genomic, RefSeqGene"/>
 *                       &lt;enumeration value="HGVS, genomic, LRG"/>
 *                       &lt;enumeration value="HGVS, coding, RefSeq"/>
 *                       &lt;enumeration value="HGVS, coding, LRG"/>
 *                       &lt;enumeration value="HGVS, coding"/>
 *                       &lt;enumeration value="HGVS, RNA"/>
 *                       &lt;enumeration value="HGVS, previous"/>
 *                       &lt;enumeration value="HGVS, protein"/>
 *                       &lt;enumeration value="HGVS, protein, RefSeq"/>
 *                       &lt;enumeration value="HGVS, nucleotide"/>
 *                       &lt;enumeration value="HGVS, non-validated"/>
 *                       &lt;enumeration value="HGVS, uncertain"/>
 *                       &lt;enumeration value="FunctionalConsequence"/>
 *                       &lt;enumeration value="ISCNCoordinates"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="Change" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Citation" type="{}CitationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="XRef" type="{}XrefType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Comment" type="{}CommentType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MeasureSetAttributeSet", propOrder = {
    "attribute",
    "citation",
    "xRef",
    "comment"
})
public class MeasureSetAttributeSet {

    @XmlElement(name = "Attribute", required = true)
    protected MeasureSetAttributeSet.Attribute attribute;
    @XmlElement(name = "Citation")
    protected List<CitationType> citation;
    @XmlElement(name = "XRef")
    protected List<XrefType> xRef;
    @XmlElement(name = "Comment")
    protected List<CommentType> comment;

    /**
     * Gets the value of the attribute property.
     * 
     * @return
     *     possible object is
     *     {@link MeasureSetAttributeSet.Attribute }
     *     
     */
    public MeasureSetAttributeSet.Attribute getAttribute() {
        return attribute;
    }

    /**
     * Sets the value of the attribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link MeasureSetAttributeSet.Attribute }
     *     
     */
    public void setAttribute(MeasureSetAttributeSet.Attribute value) {
        this.attribute = value;
    }

    /**
     * Gets the value of the citation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the citation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCitation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CitationType }
     * 
     * 
     */
    public List<CitationType> getCitation() {
        if (citation == null) {
            citation = new ArrayList<CitationType>();
        }
        return this.citation;
    }

    /**
     * Gets the value of the xRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XrefType }
     * 
     * 
     */
    public List<XrefType> getXRef() {
        if (xRef == null) {
            xRef = new ArrayList<XrefType>();
        }
        return this.xRef;
    }

    /**
     * Gets the value of the comment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommentType }
     * 
     * 
     */
    public List<CommentType> getComment() {
        if (comment == null) {
            comment = new ArrayList<CommentType>();
        }
        return this.comment;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;>AttributeType">
     *       &lt;attribute name="Type" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Description"/>
     *             &lt;enumeration value="MolecularConsequence"/>
     *             &lt;enumeration value="HGVS"/>
     *             &lt;enumeration value="HGVS, genomic, top level"/>
     *             &lt;enumeration value="HGVS, genomic, top level, previous"/>
     *             &lt;enumeration value="HGVS, genomic, top level, other"/>
     *             &lt;enumeration value="HGVS, genomic, RefSeqGene"/>
     *             &lt;enumeration value="HGVS, genomic, LRG"/>
     *             &lt;enumeration value="HGVS, coding, RefSeq"/>
     *             &lt;enumeration value="HGVS, coding, LRG"/>
     *             &lt;enumeration value="HGVS, coding"/>
     *             &lt;enumeration value="HGVS, RNA"/>
     *             &lt;enumeration value="HGVS, previous"/>
     *             &lt;enumeration value="HGVS, protein"/>
     *             &lt;enumeration value="HGVS, protein, RefSeq"/>
     *             &lt;enumeration value="HGVS, nucleotide"/>
     *             &lt;enumeration value="HGVS, non-validated"/>
     *             &lt;enumeration value="HGVS, uncertain"/>
     *             &lt;enumeration value="FunctionalConsequence"/>
     *             &lt;enumeration value="ISCNCoordinates"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="Change" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Attribute
        extends AttributeType
    {

        @XmlAttribute(name = "Type", required = true)
        protected String type;
        @XmlAttribute(name = "Change")
        @XmlSchemaType(name = "anySimpleType")
        protected String change;

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets the value of the change property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getChange() {
            return change;
        }

        /**
         * Sets the value of the change property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setChange(String value) {
            this.change = value;
        }

    }

}
