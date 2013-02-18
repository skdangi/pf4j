/*
 * Copyright 2012 Decebal Suiu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ro.fortsoft.pf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Read the plugin descriptor from the manifest file.
 *
 * @author Decebal Suiu
 */
public class DefaultPluginDescriptorFinder implements PluginDescriptorFinder {

	@Override
	public PluginDescriptor find(File pluginRepository) throws PluginException {
    	// TODO it's ok with classes/ ?
        //File manifestFile = new File(pluginRepository, "classes/META-INF/MANIFEST.MF");
		File manifestFile = new File(pluginRepository, "plugin.properties");
        if (!manifestFile.exists()) {
            // not found a 'plugin.xml' file for this plugin
            throw new PluginException("Cannot find '" + manifestFile + "' file");
        }

    	FileInputStream input = null;
		try {
			input = new FileInputStream(manifestFile);
		} catch (FileNotFoundException e) {
			// not happening 
		}
		Properties prop = new Properties();
		
		
    	//Manifest manifest = null;
        try {
            //manifest = new Manifest(input);
        	prop.load(input);
        } catch (IOException e) {
            throw new PluginException(e.getMessage(), e);
        } finally {
            try {
				input.close();
			} catch (IOException e) {
				throw new PluginException(e.getMessage(), e);
			}
        } 
        
        PluginDescriptor pluginDescriptor = new PluginDescriptor();
        
        // TODO validate !!!
        //Attributes attrs = manifest.getMainAttributes();
        //String id = attrs.getValue("Plugin-Id");
        String id = prop.getProperty("plugin.id");
        if (isEmpty(id)) {
        	throw new PluginException("plugin.id cannot be empty");
        }
        pluginDescriptor.setPluginId(id);
        
        String clazz = prop.getProperty("plugin.class");
        //String clazz = attrs.getValue("Plugin-Class");
        if (isEmpty(clazz)) {
        	throw new PluginException("plugin.class cannot be empty");
        }
        pluginDescriptor.setPluginClass(clazz);
        
        String version = prop.getProperty("plugin.version");
        //String version = attrs.getValue("Plugin-Version");
        if (isEmpty(version)) {
        	throw new PluginException("plugin.version cannot be empty");
        }
        pluginDescriptor.setPluginVersion(PluginVersion.createVersion(version));
        
        String provider = prop.getProperty("plugin.provider");
        //String provider = attrs.getValue("Plugin-Provider");
        pluginDescriptor.setProvider(provider);    
        String dependencies = prop.getProperty("plugin.dependencies");
        //String dependencies = attrs.getValue("Plugin-Dependencies");
        pluginDescriptor.setDependencies(dependencies);

		return pluginDescriptor;
	}
    
	private boolean isEmpty(String value) {
		return (value == null) || value.isEmpty();
	}
}
