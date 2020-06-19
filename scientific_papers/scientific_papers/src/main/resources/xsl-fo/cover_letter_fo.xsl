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
					<xsl:if test="ns:cover_letter">
					<fo:block font-family="sans-serif" font-size="14px" font-weight="bold" >
	       				<xsl:for-each select="ns:cover_letter/ns:cover_letter_header">       			
	               			<xsl:value-of select="."/>
	               			<fo:block></fo:block>
	               
	       				</xsl:for-each>
					</fo:block>
					</xsl:if>
				</fo:block>
				
			<fo:block>
			<xsl:if test="ns:cover_letter/ns:sender">
			<fo:block  font-family="sans-serif">
				<fo:block>Sender</fo:block>
				<xsl:for-each select="ns:cover_letter/ns:sender"> 
	       			<fo:block> 			
	               	<xsl:value-of select="ns:sender_name"/>
	       			</fo:block>  
	       			<fo:block></fo:block>
	             	<xsl:value-of select="ns:university_name"/>
	             	<fo:block></fo:block>
	            	<xsl:value-of select="ns:university_address"/>
	            	<fo:block></fo:block>
	             	<xsl:value-of select="ns:phone_number"/>
	             	<fo:block></fo:block>
	             	<xsl:value-of select="ns:email"/>
	             	<fo:block></fo:block>
				</xsl:for-each>           
			</fo:block>
			</xsl:if>
			</fo:block>
			
			<fo:block>
			<xsl:if test="ns:cover_letter/ns:recipient">
			<fo:block  font-family="sans-serif">
				<fo:block>Recipient</fo:block>
				<xsl:for-each select="ns:cover_letter/ns:recipient"> 
	       			<fo:block> 			
	               	<xsl:value-of select="ns:recipient_name"/>
	       			</fo:block>  
	       			<fo:block></fo:block>
	             	<xsl:value-of select="ns:recipient_role"/>
	             	<fo:block></fo:block>
	            	<xsl:value-of select="ns:journal_name"/>
	            	<fo:block></fo:block>
				</xsl:for-each>           
			</fo:block>
			</xsl:if>
			</fo:block>
				
	<fo:block>
	 	           <xsl:if test="ns:cover_letter/ns:date">
	 	          		<fo:block text-align-last="left">
	                    <fo:block font-family="sans-serif" position="absolute"  padding="10px">
	                    <fo:block>Date:</fo:block>                   		
	      				<xsl:value-of select="ns:cover_letter/ns:date"/>;
	      				</fo:block>
	                    </fo:block>
					</xsl:if>
				</fo:block>
				
				<fo:block>
	 	           <xsl:if test="ns:cover_letter/ns:cover_letter_body">
	 	          		<fo:block text-align-last="left">
	                    <fo:block font-family="sans-serif" position="absolute"  padding="10px">
	                    <fo:block>:</fo:block>                   		
	      				<xsl:value-of select="ns:cover_letter/ns:cover_letter_body"/>;
	      				</fo:block>
	                    </fo:block>
					</xsl:if>
				</fo:block>
		
		<fo:block>
			<xsl:if test="ns:cover_letter/ns:cover_letter_closing">
			<fo:block  font-family="sans-serif">
				<fo:block></fo:block>
				<xsl:for-each select="ns:cover_letter/ns:cover_letter_closing"> 
	       			<fo:block> 			
	               	<xsl:value-of select="ns:sender_name"/>
	       			</fo:block>  
	       			<fo:block></fo:block>
	             	<xsl:value-of select="ns:academic_title"/>
	             	<fo:block></fo:block>
	            	<xsl:value-of select="ns:department_name"/>
	            	<fo:block></fo:block>
	            	<xsl:value-of select="ns:university_name"/>
	            	<fo:block></fo:block>
				</xsl:for-each>           
			</fo:block>
			</xsl:if>
			</fo:block>
		
				
		
              
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>