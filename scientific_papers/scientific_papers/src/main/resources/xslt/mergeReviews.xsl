<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="https://github.com/AnaMijailovic/XML-Tim19"
                version="2.0">
    <xsl:import href="evaluation_form.xsl"/>
    <xsl:template match="/">
        
            	<xsl:for-each select="ns:evaluation-forms/ns:evaluation_form">
            		<xsl:apply-templates select="."></xsl:apply-templates>
            	</xsl:for-each>
    </xsl:template>
</xsl:stylesheet> 