package com.smash.revolance.ui.server;

/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ui-monitoring-server
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2012 - 2014 RevoLance
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.io.UnderscoredCamelCaseResolver;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class ITest extends JUnitStories
{
    private final CrossReference xref = new CrossReference();
    private Configuration configuration;

    public ITest()
    {
        Embedder embedder = new Embedder();
        embedder.embedderControls()
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(false)
                .doIgnoreFailureInView(true)
                .useThreads(1) // Only one thread since we use RestAssured
                .useStoryTimeoutInSecs(1800);
        useEmbedder(embedder);
    }

    @Override
    public InjectableStepsFactory stepsFactory()
    {
        return new InstanceStepsFactory(configuration(), new BrowserSteps(), this);
    }

    @Override
    public Configuration configuration()
    {
        if(configuration==null)
        {
            Class<? extends Embeddable> embeddableClass = this.getClass();
            Properties viewResources = new Properties();
            viewResources.put("decorateNonHtml", "true");
            // Start from default ParameterConverters instance
            ParameterConverters parameterConverters = new ParameterConverters();
            // factory to allow parameter conversion and loading from external
            // resources (used by StoryParser too)
            ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(new LocalizedKeywords(),
                    new LoadFromClasspath(embeddableClass), parameterConverters);
            // add custom converters
            parameterConverters.addConverters(new ParameterConverters.DateConverter(new SimpleDateFormat("yyyy-MM-dd")),
                    new ParameterConverters.ExamplesTableConverter(examplesTableFactory));

            configuration = new MostUsefulConfiguration()
                    .useStoryControls(new StoryControls()
                            .doDryRun(false)
                            .doSkipScenariosAfterFailure(false))
                    .useStoryLoader(new LoadFromClasspath(embeddableClass))
                    .useStoryParser(new RegexStoryParser(examplesTableFactory))
                    .useStoryPathResolver(new UnderscoredCamelCaseResolver())
                    .useStoryReporterBuilder(new StoryReporterBuilder()
                            .withCodeLocation(CodeLocations.codeLocationFromClass(embeddableClass))
                            .withDefaultFormats()
                            .withPathResolver(new FilePrintStreamFactory.ResolveToPackagedName())
                            .withViewResources(viewResources)
                            .withFormats(StoryReporterBuilder.Format.HTML, StoryReporterBuilder.Format.STATS, StoryReporterBuilder.Format.CONSOLE)
                            .withFailureTrace(true)
                            .withFailureTraceCompression(true).withCrossReference(xref))
                    .useParameterConverters(parameterConverters)
                    .useStepMonitor(xref.getStepMonitor())
                    .useFailureStrategy(new FailingUponPendingStep());
        }
        return configuration;
    }

    @Override
    protected List<String> storyPaths()
    {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()), "**/*.story", "");
    }
}