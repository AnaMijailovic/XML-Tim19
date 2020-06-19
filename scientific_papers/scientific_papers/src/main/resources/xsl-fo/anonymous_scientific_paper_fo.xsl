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
                   <xsl:if test="ns:scientific_paper/ns:head">
                        <fo:block text-align-last="center">
                           <fo:block font-family="sans-serif" font-size="36px" font-weight="bold" position="absolute"  padding="10px">
 								<xsl:for-each select="ns:scientific_paper/ns:head/ns:title">       			
               						<xsl:value-of select="."/>
               						<fo:block></fo:block>
             						
             							<fo:block></fo:block>
               
       						</xsl:for-each>                    		</fo:block>
                        </fo:block>
					</xsl:if>
				</fo:block>
				<fo:block>
                   <xsl:if test="ns:scientific_paper/ns:head">
                        <fo:block text-align-last="center">
                 		 <fo:block font-family="sans-serif">
                 		 		Recieved date: 
                   				<xsl:value-of select="ns:scientific_paper/ns:head/ns:recieved_date"/>	
                   		 </fo:block>
                   		<fo:block font-family="sans-serif">
                   				Revised date:
                    			<xsl:value-of select="ns:scientific_paper/ns:head/ns:revised_date"/>	
                        </fo:block>
                         <fo:block font-family="sans-serif">
                    			Accepted date:
                    			<xsl:value-of select="ns:scientific_paper/ns:head/ns:accepted_date"/>	
                        </fo:block>
                    </fo:block>
					</xsl:if>
				</fo:block>
				
				
				
	
	
	
				<fo:block>
                   <xsl:if test="ns:scientific_paper/ns:head/ns:keyword">
                        <fo:block text-align-last="left">
                           <fo:block font-family="sans-serif" font-size="10px" font-weight="bold" position="absolute"  padding="10px">                   		
            				<xsl:value-of select="ns:scientific_paper/ns:head/ns:keyword"/>;
                     		</fo:block>
                        </fo:block>
					</xsl:if>
				</fo:block>
             <fo:block>
                   <xsl:if test="ns:scientific_paper/ns:body/ns:abstract">
                        <fo:block >
                           <fo:block font-family="sans-serif"  position="absolute"  padding="10px">
                           <fo:block font-size="14px" font-weight="bold" >Abstract</fo:block>
 								<xsl:for-each select="ns:scientific_paper/ns:body/ns:abstract/ns:paragraph">       			
               						<xsl:value-of select="."/>
               						<fo:block></fo:block>
             						
             							<fo:block></fo:block>
               
       						</xsl:for-each>                    		</fo:block>
                        </fo:block>
					</xsl:if>
				</fo:block>
		<fo:block>
	<xsl:if test="ns:scientific_paper/ns:body/ns:chapter">
		<fo:block  font-family="sans-serif">
		
       <xsl:for-each select="ns:scientific_paper/ns:body/ns:chapter"> 
       <fo:block font-size="14px" font-weight="bold"> 			
               <xsl:value-of select="ns:heading"/>
       </fo:block>     
               <fo:block></fo:block>
             	<xsl:value-of select="ns:paragraph"/>
             	
             	<fo:block></fo:block>
             	<fo:block></fo:block>
             	<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:image">
	    			<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:image"/>	
	   			</xsl:if>
	   			<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:table">						
	   				<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:table"/>	
	     		</xsl:if>
	     		<xsl:if test="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:quote">						
	   				<xsl:value-of select="ns:scientific_paper/ns:body/ns:chapter/ns:paragraph/ns:quote"/>	
	     		</xsl:if>
               
       </xsl:for-each>
	</fo:block>
	</xsl:if>
	</fo:block>
	 <fo:block>
                   <xsl:if test="ns:scientific_paper/ns:body/ns:references">
                        <fo:block text-align-last="justify">
                           <fo:block font-family="sans-serif" font-size="14px" font-weight="bold" position="absolute"  padding="10px">
                           		                        References     				
                    		</fo:block>
                    		<fo:block></fo:block>
							<xsl:for-each select="ns:scientific_paper/ns:body/ns:references/ns:reference">
               				<xsl:value-of select="."/>
            				<fo:block></fo:block>
             						
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