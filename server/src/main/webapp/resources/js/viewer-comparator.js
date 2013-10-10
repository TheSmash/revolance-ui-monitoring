(function($,W,D){
/*
    $.compareSitemapToJSON = function( sitemap, refSitemap )
    {
        var comparison = $.compareSitemap( sitemap, refSitemap )
        return JSON.stringify( comparison, null, 4 );
    };

    $.compareSitemap = function( sitemap, sitemapRef )
    {
        var differencies = Array();
        var toBeMatched = Array();

        $.each(sitemapRef.sitemap, function(i, page){
            toBeMatched.push( page );
        });

        $.each(sitemap.sitemap, function(i, page){
            var refPage = findRefPage( sitemapRef, page );
            if( refPage !== undefined )
            {
                del( refPage, toBeMatched );
                differencies.push( $.comparePage( page, refPage ) );
            }
            else
            {
                differencies.push( $.comparePage( page, undefined ) );
            }
        });

        $.each(toBeMatched, function(i, page){
            differencies.push( $.comparePage( undefined, page ) );
        });

        var diff = Object();
        diff.pageComparisons = differencies;

        return diff;
    };

    $.compareVariants = function( page, refPage )
    {
        var variants = page.variants;
        var refVariants = refPage.variants;

        var differencies = Array();
        var toBeMatched = Array();

        if( page.variants !== undefined)
        {
            if( page.variants.length > 0 )
            {
                $.each(page.variants, function(i, variant){
                    toBeMatched.push( variant );
                });

                $.each(page.variants, function(i, variant){
                    var refVariant = findRefVariant( refPage, variant );
                    if( refVariant !== undefined )
                    {
                        del( refVariant, toBeMatched );
                        differencies.push( $.compareVariant( variant, refVariant ) );
                    }
                    else
                    {
                        differencies.push( $.compareVariant( variant, undefined ) );
                    }
                });
            }
        }

        $.each(toBeMatched, function(i, variant){
            differencies.push( $.compareVariant( undefined, variant ) );
        });

        return differencies;
    };

    $.compareVariant = function( variant, refVariant )
    {
        return $.comparePage( variant, refVariant, true );
    };

    var del = function(item, array)
    {
        var idx = array.indexOf( item );
        if(idx != -1)
        {
            array.splice(idx, 1);
        }
    };

// Page element comparison

    $.comparePageContent = function( page, refPage )
    {
        var newContent = Array();
        if( page !== undefined )
        {
            $.each(page.content, function(idx, element)
            {
                element.page = undefined;
                newContent.push( element );
            });
        }

        var comparisons = Array();
        if( refPage !== undefined )
        {
            var pageRefContent = refPage.content;

            // Handle common and removed elements
            $.each(pageRefContent, function(idx, refElement)
            {
                var element = containsElement( newContent, refElement );
                var comparison = $.comparePageElement( element, refElement );

                comparisons.push( comparison );

                // We found the element in the new & ref page content
                if( element !== undefined )
                {
                    del( element, newContent );
                }
            });
        }

        // Handle added elements
        $.each(newContent, function(idx, element)
        {
            var comparison = $.comparePageElement( element, undefined );

            comparisons.push( comparison );
        });

        return comparisons;
    };

    $.comparePageElement = function( element, refElement )
    {
        var differency = Object();

        differency.refElement = undefined;
        if( refElement !== undefined )
        {
            differency.refElement = refElement.internalId;
        }
        differency.element = undefined;
        if( element !== undefined )
        {
            differency.element = element.internalId;
        }

        differency.diffType = "BASE";

        differency.pageElementDiffTypes = getAllElementDifferencies();

        if(refElement === undefined)
        {
            differency.diffType = "ADDED";
        }
        else if(element === undefined)
        {
            differency.diffType = "DELETED";
        }
        else
        {
            differency.pageElementDiffTypes = getPageElementDiffTypes( element, refElement );
        }

        return differency;
    }

    var getAllElementDifferencies = function()
    {
        var elementDiffTypes = Array();

        elementDiffTypes.push( "TARGET" );
        elementDiffTypes.push( "VALUE" );
        elementDiffTypes.push( "POS" );
        elementDiffTypes.push( "LOOK" );
        elementDiffTypes.push( "IMPL" );

        return elementDiffTypes;
    }

    var getPageElementDiffTypes = function( element, refElement )
    {
        if( element === undefined || refElement === undefined )
        {
            return getAllElementDifferencies();
        }
        else
        {
            var pageElementDiffTypes = Array();
            if(!targetEquals(element, refElement))
            {
                pageElementDiffTypes.push( "TARGET" );
            }
            if(!valueEquals(element, refElement))
            {
                pageElementDiffTypes.push( "VALUE" );
            }
            if(!positionEquals(element, refElement))
            {
                pageElementDiffTypes.push( "POS" );
            }
            if(!lookEquals(element, refElement) || !sizeEquals(element, refElement))
            {
                pageElementDiffTypes.push( "LOOK" );
            }
            if(!implEquals(element, refElement))
            {
                pageElementDiffTypes.push( "IMPL" );
            }
        }
        return pageElementDiffTypes;
    }


    var containsElement = function( content, element )
    {
        if(element === undefined)
        {
            return undefined;
        }
        else
        {
            var elementFound = undefined;
            $.each(content, function(idx, elem){
                if( elementEquals(elem, element) )
                {
                    elementFound = elem;
                    return false;
                }
            });
            return elementFound;
        }
    };

    var implEquals = function( element, refElement)
    {
        return element.impl === refElement.impl;
    };

    var elementEquals = function( element, refElement )
    {
        return targetEquals(element, refElement)
                && valueEquals(element, refElement)
                && positionEquals(element, refElement)
                && sizeEquals(element, refElement)
                && lookEquals(element, refElement)
                && implEquals(element, refElement);
    };

    var targetEquals = function( element, refElement )
    {
        return element.href === refElement.href;
    };

    var valueEquals = function( element, refElement )
    {
        return getValue(element) === getValue(refElement);
    };

    var positionEquals = function( element, refElement )
    {
        return element.x === refElement.x && element.y === refElement.y;
    };

    var sizeEquals = function( element, refElement )
    {
        return element.w === refElement.w && element.h === refElement.h;
    }

// Page comparison
    $.comparePage = function( page, pageRef, withoutVariants )
    {
        var pageDifferency = Object();

        pageDifferency.refPage = undefined;
        if( pageRef !== undefined )
        {
            pageDifferency.refPage = pageRef.id;
        }

        pageDifferency.page = undefined;
        if( page !== undefined )
        {
            pageDifferency.page = page.id;
        }

        pageDifferency.diffType = "BASE";
        pageDifferency.pageDiffTypes = getAllPageDiffTypes();

        if(pageRef === undefined)
        {
            pageDifferency.diffType = "ADDED";
        }
        else if(page === undefined)
        {
            pageDifferency.diffType = "DELETED";
        }
        else
        {
            pageDifferency.pageDiffTypes = getPageDiffTypes( page, pageRef );
        }

        pageDifferency.contentComparisons = $.comparePageContent( page, pageRef );

        if( withoutVariants !== undefined )
        {
            pageDifferency.variantComparisons = $.compareVariants( page, pageRef );
        }

        return pageDifferency;
    };

    var getAllPageDiffTypes = function()
    {
        var pageDiffTypes = Array();

        pageDiffTypes.push( "URL" );
        pageDiffTypes.push( "LAYOUT" );
        pageDiffTypes.push( "CONTENT" );
        pageDiffTypes.push( "LOOK" );
        pageDiffTypes.push( "TITLE" );

        return pageDiffTypes;
    }

    var getPageDiffTypes = function( page, refPage )
    {
        var pageDiffTypes = Array();

        if(!urlEquals(page, refPage))
        {
            pageDiffTypes.push( "URL" );
        }
        if(!layoutEquals(getPageLayout(page), refPage))
        {
            pageDiffTypes.push( "LAYOUT" );
        }
        if(!contentEquals(getPageContentAsValue(page), refPage))
        {
            pageDiffTypes.push( "CONTENT" );
        }
        if(!lookEquals(page, refPage))
        {
            pageDiffTypes.push( "LOOK" );
        }
        if(!titleEquals(page, refPage))
        {
            pageDiffTypes.push( "TITLE" );
        }
        return pageDiffTypes;
    }

//-| trying to mach a page from look / layout / content

    var findRefPage = function(sitemapRef, page)
    {
        var refPage = findPageByUrl( page, sitemapRef );
        if( refPage === undefined )
        {
            refPage = findPageByUrl( page, sitemapRef );
        }
        if( refPage === undefined )
        {
            refPage = findPageByTitle( page, sitemapRef );
        }
        if( refPage === undefined )
        {
            refPage = findPageByContent( page, sitemapRef );
        }
        if( refPage === undefined )
        {
            refPage = findPageByLook( page, sitemapRef );
        }
        return refPage;
    };

    var findRefVariant = function(refPage, variant)
    {
        if( refPage.variants !== undefined )
        {
            var refVariants = refPage.variants;

            var refVariant = findVariantByUrl( variant, refVariants );
            if( refVariant === undefined )
            {
                refVariant = findVariantBySource( variant, refVariants );
            }
            if( refVariant === undefined )
            {
                refVariant = findVariantByLook( variant, refVariants );
            }
        }
        return refVariant;
    }

//-| look for a page by source
    var findVariantBySource = function( variants, variant )
    {
        var ret;
        $.each(variants, function(i, val){
            if(sourceEquals(variant, val))
            {
                ret = val;
                return false;
            }
        });
        return ret;
    };

    var sourceEquals = function( sourceElement, refSourceElement )
    {
        var comparison = $.comparePageElement( sourceElement, refSourceElement);
        return comparison.diffType === "BASE";
    };

//-| look for a page by title
    var findPageByTitle = function( page, sitemap )
    {
        var ret;
        $.each(sitemap.sitemap, function(i, val){
            if(titleEquals(page, val))
            {
                ret = val;
                return false;
            }
        });
        return ret;
    };

    var getTitle = function( domain, url )
    {
        return url.substring( domain.length );
    };

//-| look for a page by url
    var findPageByUrl = function( page, sitemap )
    {
        var ret;
        $.each(sitemap.sitemap, function(i, val){
            if(urlEquals(page, val))
            {
                ret = val;
                return false;
            }
        });
        return ret;
    };

    var findVariantByUrl = function( variant, variants )
    {
        var ret;
        $.each(variants, function(i, val){
            if(urlEquals(variant, val))
            {
                ret = val;
                return false;
            }
        });
        return ret;
    };

    var getRelUrl = function( domain, url )
    {
        return url.substring( domain.length );
    };

//-| look for a page by look
    var findPageByLook = function( page, sitemap )
    {
        var ret;
        $.each(sitemap.sitemap, function(i, val){
            if(lookEquals(page, val))
            {
                ret = val;
                return false;
            }
        });
        return ret;
    };

    var findVariantByLook = function( variants, variant )
    {
        var ret;
        $.each(variants, function(i, val){
            if(lookEquals(variant, val))
            {
                ret = val;
                return false;
            }
        });
        return ret;
    };

//-| look for a page by content or layout
    var findPageByContent = function( page, sitemap )
    {
        var ret;
        var pageContent = getPageContentAsValue( page );
        $.each(sitemap.sitemap, function(i, e){
            if(contentEquals(pageContent, e))
            {
                ret = e;
                return false;
            }
        });
        return ret;
    };

    var findPageByLayout = function( page, sitemap )
    {
        var ret;
        var pageLayout = getPageLayout( page );
        $.each(sitemap.sitemap, function(i, e){
            if(layoutEquals(pageLayout, e))
            {
                ret = e;
                return false;
            }
        });
        return ret;
    };

    var titleEquals = function( page, pageRef )
    {
        return page.title === pageRef.title;
    };

    var urlEquals = function( page, pageRef )
    {
        return page.url === pageRef.url;
    };

    var lookEquals = function( page, pageRef )
    {
        return page.checksum === pageRef.checksum;
    };

    var layoutEquals = function( pageLayout, pageRef )
    {
        var layoutEq = true;
        var pageRefLayout = getPageLayout( pageRef );
        $.each(pageRefLayout, function(i, refElem)
        {
            var match = false;
            $.each(pageLayout, function(idx, elem)
            {
                if( refElem.x === elem.x && refElem.y === elem.y && refElem.w === elem.w && refElem.h === elem.h)
                {
                   match = true;
                   return false;
                }
            });
            if(!match)
            {
                layoutEq = false;
                return false;
            }
        });
        return layoutEq;
    };

    var contentEquals = function( pageContent, pageRef )
    {
        var contentEq = true;

        var pageRefContent = getPageContentAsValue( pageRef );
        var matchedElements = Array();
        $.each(pageContent, function(i, val)
        {
            matchedElements.push( val );
        });

        $.each(pageRefContent, function(i , val)
        {
            var match = false;
            $.each(pageContent, function(idx, elem)
            {
                if(matchedElements[idx] !== undefined)
                {

                    if( elem.value === val.value && elem.type === val.type )
                    {
                        match = true;
                        matchedElements[idx] = undefined;
                        return false;
                    }

                }
            });
            if(!match)
            {
                contentEq = false;
                return false;
            }
        });
        return contentEq;
    };

    var getPageLayout = function( page )
    {
        var layout = Array();
        $.each(page.content, function(i, e){
            var elem = Object();
            elem.x = e.x;
            elem.y = e.y;
            elem.w = e.w;
            elem.h = e.h;

            layout.push( elem );
        });
        return layout
    }

    var getPageContentAsValue = function( page )
    {
        var value = Array();
        $.each(page.content, function(i, e){
            var content = Object();
            content.value = getValue( e );
            content.type = getImpl( e );

            value.push( content );
        });
        return value;
    };

//-| utilities

//-| Retrieve the value of an element
    var getValue = function( element )
    {
        if( element.txt !== "" )
        {
            return element.txt;
        }
        else if( element.value !== "" )
        {
            return element.value;
        }
        else
        {
            return "";
        }
    };

    var getImpl = function( element )
    {
        return element.impl;
    }
*/
})(jQuery, window, document);


