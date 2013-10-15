/*
 * Copyright (c) 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package issuetrackinglite.model;

import issuetrackinglite.model.Issue.IssueStatus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class TrackingServiceStub implements TrackingService {

	final static String projectsXML = "projects.xml";
	
    // You add a project by adding an entry with an empty observable array list
    // of issue IDs in the projects Map.
    final ObservableMap<String, ObservableList<String>> projectsMap;
    {
        final Map<String, ObservableList<String>> map = new TreeMap<String, ObservableList<String>>();
        projectsMap = FXCollections.observableMap(map);
        
        for (String s : newList()) {
            projectsMap.put(s, FXCollections.<String>observableArrayList());
        }
    }

    // The projectNames list is kept in sync with the project's map by observing
    // the projectsMap and modifying the projectNames list in consequence.
    final MapChangeListener<String, ObservableList<String>> projectsMapChangeListener = new MapChangeListener<String, ObservableList<String>>() {
        @Override
        public void onChanged(Change<? extends String, ? extends ObservableList<String>> change) {
            if (change.wasAdded()) projectNames.add(change.getKey());
            if (change.wasRemoved()) projectNames.remove(change.getKey());
        }
    };
    final ObservableList<String> projectNames;
    {
        projectNames = FXCollections.<String>observableArrayList();
        projectNames.addAll(projectsMap.keySet());
        projectsMap.addListener(projectsMapChangeListener);
    }

    // A Issue stub.
    public final class IssueStub implements ObservableIssue {
        private final SimpleStringProperty id;
        private final SimpleStringProperty projectName;
        private final SimpleStringProperty title;
        private final SimpleStringProperty description;
        private final SimpleObjectProperty<IssueStatus> status =
                new SimpleObjectProperty<IssueStatus>(IssueStatus.NEW);

        IssueStub(String projectName, String id) {
            this(projectName, id, null);
        }
        IssueStub(String projectName, String id, String title) {
            assert projectNames.contains(projectName);
            assert ! projectsMap.get(projectName).contains(id);
            assert ! issuesMap.containsKey(id);
            this.projectName = new SimpleStringProperty(projectName);
            this.id = new SimpleStringProperty(id);
            this.title = new SimpleStringProperty(title);
            this.description = new SimpleStringProperty("");
        }

        @Override
        public IssueStatus getStatus() {
            return status.get();
        }

        @Override
        public String getId() {
            return id.get();
        }

        @Override
        public String getProjectName() {
            return projectName.get();
        }

        @Override
        public String getSynopsis() {
            return title.get();
        }

        private void setSynopsis(String title) {
            this.title.set(title);
        }

        @Override
        public String getDescription() {
            return description.get();
        }

        private void setDescription(String description) {
            this.description.set(description);
        }

        private void setStatus(IssueStatus issueStatus) {
            this.status.set(issueStatus);
        }

        @Override
        public ObservableValue<String> idProperty() {
            return id;
        }

        @Override
        public ObservableValue<String> projectNameProperty() {
            return projectName;
        }

        @Override
        public ObservableValue<IssueStatus> statusProperty() {
            return status;
        }

        @Override
        public ObservableValue<String> synopsisProperty() {
            return title;
        }

        @Override
        public ObservableValue<String> descriptionProperty() {
            return description;
        }
    }

    // You create new issue by adding a IssueStub instance to the issuesMap.
    // the new id will be automatically added to the corresponding list in
    // the projectsMap.
    //
    final MapChangeListener<String, IssueStub> issuesMapChangeListener = new MapChangeListener<String, IssueStub>() {
        @Override
        public void onChanged(Change<? extends String, ? extends IssueStub> change) {
            if (change.wasAdded()) {
                final IssueStub val = change.getValueAdded();
                projectsMap.get(val.getProjectName()).add(val.getId());
            }
            if (change.wasRemoved()) {
                final IssueStub val = change.getValueRemoved();
                projectsMap.get(val.getProjectName()).remove(val.getId());
            }
        }
    };
    
    final AtomicInteger issueCounter = new AtomicInteger(0);
    final ObservableMap<String, IssueStub> issuesMap;
    {
        final Map<String, IssueStub> map = new TreeMap<String, IssueStub>();
        issuesMap = FXCollections.observableMap(map);
        issuesMap.addListener(issuesMapChangeListener);
        IssueStub ts;
        
        // Load the xml as property and return as TreeMap
        TreeMap<String, String> projectsTreeMap = property2TreeMap(projectsXML);
	
        // For each project
        for(Entry<String, String> projectsEntry: projectsTreeMap.entrySet()){
			String synopsesXML = projectsEntry.getValue().toString();
			TreeMap<String, String> synopsesTreeMap = property2TreeMap(synopsesXML + ".xml");
			
			ArrayList<String> synopsesList = new ArrayList<String>();
			ArrayList<String> descriptionsList = new ArrayList<String>();

			// For each pair of synopsis & description
			for(Entry<String, String> synopsesEntry: synopsesTreeMap.entrySet()){
				// Minus 1 because ArrayList starts from 0
				if(synopsesEntry.getKey().startsWith("synopsis")){
					synopsesList.add(Integer.valueOf(synopsesEntry.getKey().replace("synopsis", "")) - 1, synopsesEntry.getValue());
				} else if(synopsesEntry.getKey().startsWith("description")){
					descriptionsList.add(Integer.valueOf(synopsesEntry.getKey().replace("description", "")) - 1, synopsesEntry.getValue());
				}
			}
			
			for(int i = 0; i < synopsesList.size(); i++){
				ts = createIssueFor(synopsesXML);
		        ts.setSynopsis(synopsesList.get(i));
		        ts.setDescription(descriptionsList.get(i));
			}
        }
    }

    // Create the list for listView
    private static List<String> newList() {
    	
    	ArrayList<String> stringList = new ArrayList<String>();
    	
        // By Chris Wong
        TreeMap<String, String> projectsTreeMap = (TreeMap<String, String>) property2TreeMap(projectsXML);
		
        for(Entry<String, String> projectsEntry: projectsTreeMap.entrySet()){
        	String projectName = projectsEntry.getValue();
        	stringList.add(projectName);
        }
        return stringList;
    }


    @Override
    public IssueStub createIssueFor(String projectName) {
        assert projectNames.contains(projectName);
        final IssueStub issue = new IssueStub(projectName, "TT-"+issueCounter.incrementAndGet());
        assert issuesMap.containsKey(issue.getId()) == false;
        assert projectsMap.get(projectName).contains(issue.getId()) == false;
        issuesMap.put(issue.getId(), issue);
        return issue;
    }

    @Override
    public void deleteIssue(String issueId) {
        assert issuesMap.containsKey(issueId);
        issuesMap.remove(issueId);
    }

    @Override
    public ObservableList<String> getProjectNames() {
        return projectNames;
    }

    @Override
    public ObservableList<String> getIssueIds(String projectName) {
        return projectsMap.get(projectName);
    }

    @Override
    public IssueStub getIssue(String issueId) {
        return issuesMap.get(issueId);
    }

    @Override
    public void saveIssue(String issueId, IssueStatus status,
            String synopsis, String description) {
        IssueStub issue = getIssue(issueId);
        issue.setDescription(description);
        issue.setSynopsis(synopsis);
        issue.setStatus(status);
    }

    // Load the xml as property and return as TreeMap
    public static TreeMap<String, String> property2TreeMap(String xmlFilename){
    	java.util.Properties props = new java.util.Properties();
        
        try {
			props.loadFromXML(new FileInputStream(xmlFilename));
        } catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        Map<String, String> unsortMap = new HashMap<String, String>();
		for(Map.Entry<Object, Object> projectEntry: props.entrySet()){
			unsortMap.put(projectEntry.getKey().toString(), projectEntry.getValue().toString());
		}
		
		TreeMap<String, String> treeMap = new TreeMap<String, String>(unsortMap);
		return treeMap;
    }
}
