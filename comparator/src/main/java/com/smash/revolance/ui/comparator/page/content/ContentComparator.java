package com.smash.revolance.ui.comparator.page.content;

import com.smash.revolance.ui.comparator.NoMatchFound;
import com.smash.revolance.ui.comparator.element.*;
import com.smash.revolance.ui.model.diff.DiffType;
import com.smash.revolance.ui.model.element.api.ElementBean;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: wsmash
 * Date: 09/06/13
 * Time: 12:58
 */
public class ContentComparator implements IContentComparator
{
    IElementMatchMaker elementMatcher = new ElementMatchMaker();

    IElementComparator elementComparator = new ElementComparator();

    @Override
    public Collection<ElementComparison> compare(Collection<ElementBean> content, Collection<ElementBean> reference)
    {
        List<ElementComparison> comparisons = new ArrayList();

        List<ElementBean> addedElements = new ArrayList( content );
        for ( ElementBean element : content )
        {
            ElementComparison comparison = null;
            try
            {
                Collection<ElementMatch> matches = elementMatcher.findMatch( reference, element, ElementSearchMethod.VALUE, ElementSearchMethod.LOOK );
                ElementMatch match = elementMatcher.getBestMatch( matches );

                reference.remove( match.getReference() );
                addedElements.remove( match.getMatch() );

                Collection<ElementDifferency> differencies = elementComparator.compare( element, match.getReference() );

                if(differencies.isEmpty())
                {
                    comparison = BaseComparison( match );
                }
                else
                {
                    comparison = ChangedComparison(match);
                    comparison.setElementDifferencies( differencies );
                }
            }
            catch (NoMatchFound noMatchFound)
            {
                comparison = new DeletedComparison( element ).invoke();
            }
            catch (InvalidParameterException e)
            {
                // comparison = new BaseComparison( element ).invoke();
            }

            if ( comparison != null )
            {
                addedElements.remove( element );
                comparisons.add( comparison );
            }
        }

        for ( ElementBean element : addedElements )
        {
            comparisons.add( new DeletedComparison( element ).invoke() );
        }

        for ( ElementBean element : reference )
        {
            new DeletedComparison( element );
            comparisons.add( new AddedComparison( element ).invoke() );
        }

        return comparisons;
    }

    private ElementComparison BaseComparison(ElementMatch match)
    {
        ElementComparison comparison;
        comparison = new ElementComparison( match );
        comparison.setDiffType( DiffType.BASE );
        return comparison;
    }

    private ElementComparison ChangedComparison(ElementMatch match)
    {
        ElementComparison comparison;
        comparison = new ElementComparison( match );
        comparison.setDiffType( DiffType.CHANGED );
        return comparison;
    }

    private class DeletedComparison
    {
        private ElementBean element;

        public DeletedComparison(ElementBean element)
        {
            this.element = element;
        }

        public ElementComparison invoke()
        {
            ElementComparison comparison;
            comparison = new ElementComparison( new ElementMatch( element ) );
            comparison.setDiffType( DiffType.DELETED );
            return comparison;
        }
    }

    private class BaseComparison
    {
        private ElementBean element;

        public BaseComparison(ElementBean element)
        {
            this.element = element;
        }

        public ElementComparison invoke()
        {
            ElementComparison comparison;
            comparison = BaseComparison( new ElementMatch().setMatch( element ) );
            return comparison;
        }
    }

    private class AddedComparison
    {
        private ElementBean element;

        public AddedComparison(ElementBean element)
        {
            this.element = element;
        }

        public ElementComparison invoke()
        {
            ElementComparison comparison;
            comparison = new ElementComparison( new ElementMatch().setMatch( element ) );
            comparison.setDiffType( DiffType.ADDED );
            return comparison;
        }
    }
}
