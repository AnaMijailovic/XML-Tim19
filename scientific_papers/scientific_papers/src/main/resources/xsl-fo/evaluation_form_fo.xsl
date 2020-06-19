<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="https://github.com/AnaMijailovic/XML-Tim19"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" version="2.0">

    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="letter-page">
                    <fo:region-body margin="0.75in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="letter-page">
                <fo:flow flow-name="xsl-region-body">
                 	
	                 	
	          	<fo:block>
					<xsl:if test="ns:evaluation_form">
					<fo:block font-family="sans-serif">
					<fo:block>Reviewer:</fo:block>
	       				<xsl:for-each select="ns:evaluation_form/ns:reviewer">       			
	               			<xsl:value-of select="concat(ns:first_name, ' ', ns:last_name)"/>
	               			<fo:block></fo:block>
	             			<xsl:value-of select="ns:email"/>
	             			<fo:block></fo:block>
	             			<fo:block></fo:block>
	               
	       				</xsl:for-each>
					</fo:block>
					</xsl:if>
				</fo:block>
				
				
				<fo:block>
	 	           <xsl:if test="ns:evaluation_form/ns:scientific_paper_title">
	 	          		<fo:block text-align-last="left">
	                    <fo:block font-family="sans-serif" position="absolute"  padding="10px">
	                    <fo:block>Scientific paper title:</fo:block>                   		
	      				<xsl:value-of select="ns:evaluation_form/ns:scientific_paper_title"/>;
	      				</fo:block>
	                    </fo:block>
					</xsl:if>
				</fo:block>
				
				
				<fo:block>
	 	           <xsl:if test="ns:evaluation_form/ns:scientific_paper_summary">
	 	          		<fo:block text-align-last="left">
	                    <fo:block font-family="sans-serif" position="absolute"  padding="10px">
	                    <fo:block>Scientific paper summary:</fo:block>                   		
	      				<xsl:value-of select="ns:evaluation_form/ns:scientific_paper_summary"/>;
	      				</fo:block>
	                    </fo:block>
					</xsl:if>
				</fo:block>
		
		
			<fo:block>
			<xsl:if test="ns:evaluation_form/ns:sugestions/ns:sugestion">
			<fo:block  font-family="sans-serif">
				<fo:block>Sugestions</fo:block>
				<xsl:for-each select="ns:evaluation_form/ns:sugestions/ns:sugestion"> 
	       			<fo:block> 			
	               	<xsl:value-of select="ns:reg"/>
	       			</fo:block>  
	       			<fo:block></fo:block>
	             	<xsl:value-of select="ns:comment"/>
				</xsl:for-each>           
			</fo:block>
			</xsl:if>
			</fo:block>
			
			
			<fo:block>
			<xsl:if test="ns:evaluation_form/ns:overall_recommendation">
			<fo:block  font-family="sans-serif">
				<fo:block>Overall recommendation</fo:block>
				<xsl:for-each select="ns:evaluation_form/ns:overall_recommendation"> 
	       			<fo:block> 			
	               	<xsl:value-of select="ns:recommendation"/>
	       			</fo:block>  
	       			<fo:block></fo:block>
	             	<xsl:value-of select="ns:comment"/>
				</xsl:for-each>           
			</fo:block>
			</xsl:if>
			</fo:block>
			
		
              
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>