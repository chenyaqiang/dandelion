/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2014 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.After;
import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;

import com.github.dandelion.core.utils.PropertiesUtils;

public class CustomProfileTest {

	@After
	public void after() {
		System.clearProperty(Profile.DANDELION_PROFILE_ACTIVE);
		System.clearProperty(DandelionConfig.ASSET_LOCATIONS_RESOLUTION_STRATEGY.getName());
		System.clearProperty(DandelionConfig.CACHE_ASSET_MAX_SIZE.getName());
	}

	@Test
	public void should_set_qa_profile() {
		System.setProperty(Profile.DANDELION_PROFILE_ACTIVE, "qa");
		Configuration config = new Configuration(new MockFilterConfig(), new Properties());
		assertThat(Profile.getActiveProfile()).isEqualTo("qa");
		assertThat(config.getActiveRawProfile()).isEqualTo("qa");
	}

	@Test
	public void should_set_qa_profile_even_with_white_spaces() {
		System.setProperty(Profile.DANDELION_PROFILE_ACTIVE, " qa");
		Configuration config = new Configuration(new MockFilterConfig(), new Properties());
		assertThat(Profile.getActiveProfile()).isEqualTo("qa");
		assertThat(config.getActiveRawProfile()).isEqualTo("qa");
	}

	@Test
	public void should_load_configuration_from_default_dev_profile_when_the_custom_profile_is_empty() {
		System.setProperty(Profile.DANDELION_PROFILE_ACTIVE, "qa");
		Configuration config = new Configuration(new MockFilterConfig(), new Properties());

		// Bundle-related configurations
		assertThat(config.getBundleLocation()).isEmpty();
		assertThat(config.getBundleIncludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.BUNDLE_INCLUDES.defaultDevValue()));
		assertThat(config.getBundleExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.BUNDLE_EXCLUDES.defaultDevValue()));

		// Asset-related configurations
		assertThat(config.isAssetMinificationEnabled()).isFalse();
		assertThat(config.getAssetLocationsResolutionStrategy()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_LOCATIONS_RESOLUTION_STRATEGY.defaultDevValue()));
		assertThat(config.getAssetProcessors()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_PROCESSORS.defaultDevValue()));
		assertThat(config.getAssetProcessorEncoding()).isEqualTo(
				DandelionConfig.ASSET_PROCESSORS_ENCODING.defaultDevValue());
		assertThat(config.getAssetCssExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_CSS_EXCLUDES.defaultDevValue()));
		assertThat(config.getAssetJsExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_JS_EXCLUDES.defaultDevValue()));

		// Caching-related configurations
		assertThat(config.isAssetCachingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.ASSET_CACHING.defaultDevValue()));
		assertThat(config.getCacheName()).isEqualTo(DandelionConfig.CACHE_NAME.defaultDevValue());
		assertThat(config.getCacheManagerName()).isEqualTo(DandelionConfig.CACHE_MANAGER_NAME.defaultDevValue());
		assertThat(config.getCacheConfigurationLocation()).isEqualTo(
				DandelionConfig.CACHE_CONFIGURATION_LOCATION.defaultDevValue());
		assertThat(config.getCacheAssetMaxSize()).isEqualTo(
				Integer.parseInt(DandelionConfig.CACHE_ASSET_MAX_SIZE.defaultDevValue()));
		assertThat(config.getCacheRequestMaxSize()).isEqualTo(
				Integer.parseInt(DandelionConfig.CACHE_REQUEST_MAX_SIZE.defaultDevValue()));

		// Tooling-related configurations
		assertThat(config.isToolAssetPrettyPrintingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_ASSET_PRETTY_PRINTING.defaultDevValue()));
		assertThat(config.isToolBundleGraphEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_BUNDLE_GRAPH.defaultDevValue()));
		assertThat(config.isToolBundleReloadingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_BUNDLE_RELOADING.defaultDevValue()));

		// Misc configurations
		assertThat(config.isMonitoringJmxEnabled())
				.isEqualTo(Boolean.parseBoolean(DandelionConfig.MONITORING_JMX.defaultDevValue()));
		assertThat(config.isServlet3Enabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.OVERRIDE_SERVLET3.defaultDevValue()));
	}

	@Test
	public void should_load_configuration_from_custom_properties_and_complete_with_default_dev_values() {
		System.setProperty(Profile.DANDELION_PROFILE_ACTIVE, "qa");

		Properties userProperties = new Properties();
		userProperties.put(DandelionConfig.ASSET_LOCATIONS_RESOLUTION_STRATEGY.getName(), "foo,bar");
		userProperties.put(DandelionConfig.CACHE_ASSET_MAX_SIZE.getName(), "40");

		Configuration config = new Configuration(new MockFilterConfig(), userProperties);

		// Bundle-related configurations
		assertThat(config.getBundleLocation()).isEmpty();
		assertThat(config.getBundleIncludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.BUNDLE_INCLUDES.defaultDevValue()));
		assertThat(config.getBundleExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.BUNDLE_EXCLUDES.defaultDevValue()));

		// Asset-related configurations
		assertThat(config.isAssetMinificationEnabled()).isFalse();
		assertThat(config.getAssetLocationsResolutionStrategy()).isEqualTo(PropertiesUtils.propertyAsList("foo,bar")); // OVERRIDEN
		assertThat(config.getAssetProcessors()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_PROCESSORS.defaultDevValue()));
		assertThat(config.getAssetProcessorEncoding()).isEqualTo(
				DandelionConfig.ASSET_PROCESSORS_ENCODING.defaultDevValue());
		assertThat(config.getAssetCssExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_CSS_EXCLUDES.defaultDevValue()));
		assertThat(config.getAssetJsExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_JS_EXCLUDES.defaultDevValue()));

		// Caching-related configurations
		assertThat(config.isAssetCachingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.ASSET_CACHING.defaultDevValue()));
		assertThat(config.getCacheName()).isEqualTo(DandelionConfig.CACHE_NAME.defaultDevValue());
		assertThat(config.getCacheManagerName()).isEqualTo(DandelionConfig.CACHE_MANAGER_NAME.defaultDevValue());
		assertThat(config.getCacheConfigurationLocation()).isEqualTo(
				DandelionConfig.CACHE_CONFIGURATION_LOCATION.defaultDevValue());
		assertThat(config.getCacheAssetMaxSize()).isEqualTo(40); // OVERRIDEN
		assertThat(config.getCacheRequestMaxSize()).isEqualTo(
				Integer.parseInt(DandelionConfig.CACHE_REQUEST_MAX_SIZE.defaultDevValue()));

		// Tooling-related configurations
		assertThat(config.isToolAssetPrettyPrintingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_ASSET_PRETTY_PRINTING.defaultDevValue()));
		assertThat(config.isToolBundleGraphEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_BUNDLE_GRAPH.defaultDevValue()));
		assertThat(config.isToolBundleReloadingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_BUNDLE_RELOADING.defaultDevValue()));

		// Misc configurations
		assertThat(config.isMonitoringJmxEnabled())
				.isEqualTo(Boolean.parseBoolean(DandelionConfig.MONITORING_JMX.defaultDevValue()));
		assertThat(config.isServlet3Enabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.OVERRIDE_SERVLET3.defaultDevValue()));
	}

	@Test
	public void should_load_configuration_from_initparams_and_complete_with_default_dev_values() {
		System.setProperty(Profile.DANDELION_PROFILE_ACTIVE, "qa");

		MockFilterConfig filterConfig = new MockFilterConfig();
		filterConfig.addInitParameter(DandelionConfig.ASSET_LOCATIONS_RESOLUTION_STRATEGY.getName(), "  foo,bar , baz");
		filterConfig.addInitParameter(DandelionConfig.CACHE_ASSET_MAX_SIZE.getName(), "30");

		Configuration config = new Configuration(filterConfig, null);

		// Bundle-related configurations
		assertThat(config.getBundleLocation()).isEmpty();
		assertThat(config.getBundleIncludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.BUNDLE_INCLUDES.defaultDevValue()));
		assertThat(config.getBundleExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.BUNDLE_EXCLUDES.defaultDevValue()));

		// Asset-related configurations
		assertThat(config.isAssetMinificationEnabled()).isFalse();
		assertThat(config.getAssetLocationsResolutionStrategy()).isEqualTo(
				PropertiesUtils.propertyAsList("foo,bar,baz")); // OVERRIDEN
		assertThat(config.getAssetProcessors()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_PROCESSORS.defaultDevValue()));
		assertThat(config.getAssetProcessorEncoding()).isEqualTo(
				DandelionConfig.ASSET_PROCESSORS_ENCODING.defaultDevValue());
		assertThat(config.getAssetCssExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_CSS_EXCLUDES.defaultDevValue()));
		assertThat(config.getAssetJsExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_JS_EXCLUDES.defaultDevValue()));

		// Caching-related configurations
		assertThat(config.isAssetCachingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.ASSET_CACHING.defaultDevValue()));
		assertThat(config.getCacheName()).isEqualTo(DandelionConfig.CACHE_NAME.defaultDevValue());
		assertThat(config.getCacheManagerName()).isEqualTo(DandelionConfig.CACHE_MANAGER_NAME.defaultDevValue());
		assertThat(config.getCacheConfigurationLocation()).isEqualTo(
				DandelionConfig.CACHE_CONFIGURATION_LOCATION.defaultDevValue());
		assertThat(config.getCacheAssetMaxSize()).isEqualTo(30); // OVERRIDEN
		assertThat(config.getCacheRequestMaxSize()).isEqualTo(
				Integer.parseInt(DandelionConfig.CACHE_REQUEST_MAX_SIZE.defaultDevValue()));

		// Tooling-related configurations
		assertThat(config.isToolAssetPrettyPrintingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_ASSET_PRETTY_PRINTING.defaultDevValue()));
		assertThat(config.isToolBundleGraphEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_BUNDLE_GRAPH.defaultDevValue()));
		assertThat(config.isToolBundleReloadingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_BUNDLE_RELOADING.defaultDevValue()));

		// Misc configurations
		assertThat(config.isMonitoringJmxEnabled())
				.isEqualTo(Boolean.parseBoolean(DandelionConfig.MONITORING_JMX.defaultDevValue()));
		assertThat(config.isServlet3Enabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.OVERRIDE_SERVLET3.defaultDevValue()));
	}

	@Test
	public void should_load_configuration_from_system_properties_and_complete_with_default_dev_values() {
		System.setProperty(Profile.DANDELION_PROFILE_ACTIVE, "qa");
		System.setProperty(DandelionConfig.ASSET_LOCATIONS_RESOLUTION_STRATEGY.getName(), "bar ,foo  ,baz,qux  ");
		System.setProperty(DandelionConfig.CACHE_ASSET_MAX_SIZE.getName(), "20");

		Configuration config = new Configuration(new MockFilterConfig(), null);

		// Bundle-related configurations
		assertThat(config.getBundleLocation()).isEmpty();
		assertThat(config.getBundleIncludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.BUNDLE_INCLUDES.defaultDevValue()));
		assertThat(config.getBundleExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.BUNDLE_EXCLUDES.defaultDevValue()));

		// Asset-related configurations
		assertThat(config.isAssetMinificationEnabled()).isFalse();
		assertThat(config.getAssetLocationsResolutionStrategy()).isEqualTo(
				PropertiesUtils.propertyAsList("bar,foo,baz,qux")); // OVERRIDEN
		assertThat(config.getAssetProcessors()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_PROCESSORS.defaultDevValue()));
		assertThat(config.getAssetProcessorEncoding()).isEqualTo(
				DandelionConfig.ASSET_PROCESSORS_ENCODING.defaultDevValue());
		assertThat(config.getAssetCssExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_CSS_EXCLUDES.defaultDevValue()));
		assertThat(config.getAssetJsExcludes()).isEqualTo(
				PropertiesUtils.propertyAsList(DandelionConfig.ASSET_JS_EXCLUDES.defaultDevValue()));

		// Caching-related configurations
		assertThat(config.isAssetCachingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.ASSET_CACHING.defaultDevValue()));
		assertThat(config.getCacheName()).isEqualTo(DandelionConfig.CACHE_NAME.defaultDevValue());
		assertThat(config.getCacheManagerName()).isEqualTo(DandelionConfig.CACHE_MANAGER_NAME.defaultDevValue());
		assertThat(config.getCacheConfigurationLocation()).isEqualTo(
				DandelionConfig.CACHE_CONFIGURATION_LOCATION.defaultDevValue());
		assertThat(config.getCacheAssetMaxSize()).isEqualTo(20); // OVERRIDEN
		assertThat(config.getCacheRequestMaxSize()).isEqualTo(
				Integer.parseInt(DandelionConfig.CACHE_REQUEST_MAX_SIZE.defaultDevValue()));

		// Tooling-related configurations
		assertThat(config.isToolAssetPrettyPrintingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_ASSET_PRETTY_PRINTING.defaultDevValue()));
		assertThat(config.isToolBundleGraphEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_BUNDLE_GRAPH.defaultDevValue()));
		assertThat(config.isToolBundleReloadingEnabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.TOOL_BUNDLE_RELOADING.defaultDevValue()));

		// Misc configurations
		assertThat(config.isMonitoringJmxEnabled())
				.isEqualTo(Boolean.parseBoolean(DandelionConfig.MONITORING_JMX.defaultDevValue()));
		assertThat(config.isServlet3Enabled()).isEqualTo(
				Boolean.parseBoolean(DandelionConfig.OVERRIDE_SERVLET3.defaultDevValue()));

		System.clearProperty(DandelionConfig.ASSET_LOCATIONS_RESOLUTION_STRATEGY.getName());
		System.clearProperty(DandelionConfig.CACHE_ASSET_MAX_SIZE.getName());
	}
}
