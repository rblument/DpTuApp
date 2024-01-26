/*
 * DPTu: Dynamic Programming Tutor
 * 
 *  (C) Johanna & Richard Blumenthal, All rights reserved
 * 
 *  Unauthorized use, duplication or distribution without the authors'
 *  permission is strictly prohibited.
 * 
 *  Unless required by applicable law or agreed to in writing, this
 *  software is distributed on an "AS IS" basis without warranties
 *  or conditions of any kind, either expressed or implied.
 */
package edu.regis.dptu.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.regis.dptu.err.NonRecoverableException;
import edu.regis.dptu.err.ObjNotFoundException;
import edu.regis.dptu.err.XmlException;
import edu.regis.dptu.model.BloomLevel;
import edu.regis.dptu.model.Course;
import edu.regis.dptu.model.ExercisingLocation;
import edu.regis.dptu.model.GuiGesture;
import edu.regis.dptu.model.Hint;
import edu.regis.dptu.model.InformationStep;
import edu.regis.dptu.model.KnowledgeComponent;
import edu.regis.dptu.model.ScaffoldLevel;
import edu.regis.dptu.model.Step;
import edu.regis.dptu.model.StepSubType;
import edu.regis.dptu.model.Task;
import edu.regis.dptu.model.TaskSelectionKind;
import edu.regis.dptu.model.Timeout;
import edu.regis.dptu.model.Unit;
import edu.regis.dptu.svc.CourseSvc;
import edu.regis.dptu.util.XmlMgr;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * An XML-based Data Access Object implementing CourseSvc behaviors.
 *
 * @author rickb
 */
public class CourseDAO implements CourseSvc {

    /**
     * Instantiate this Course DAO with default values.
     */
    public CourseDAO() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course retrieve(int id) throws ObjNotFoundException, NonRecoverableException {
        String fileName = "Course_" + id + ".xml";

        try {
            XmlMgr xmlMgr = XmlMgr.instance();
            Element root = xmlMgr.findRoot(fileName);

            Course course = new Course(id);

            course.setTitle(XmlMgr.getAttribute(root, "title"));

            course.setDescription(XmlMgr.getChild(root, "Description").getTextContent());
            
            course.setPrimaryPedagogy(extractPrimaryPedagogy(root));

            course.setUnits(extractUnits(root));

            course.setOutcomes(extractKnowledgeComponents(root));
            System.out.println("CourseDAO outcomes: " + course.getOutcomes());
            System.out.println("size: " + course.getOutcomes().size());
            
            return course;

        } catch (ObjNotFoundException e) {
            throw new ObjNotFoundException(String.valueOf(id));
        } catch (XmlException e) {
            throw new NonRecoverableException("CourseDAO_Err_1", e);
        }
    }

    /**
     * Extract child &lt;Outcome> elements from given XML DOM parent element
     * adding each as a Outcome to the given Course.
     *
     * @param parent an XML DOM Element containing one or more child
     * &lt;Outcome> node elements.
     * @throws ShaTuException a nonrecoverable exception also see getCause()
     * @return a List of KnowledgeComponent outcomes
     */
    private ArrayList<KnowledgeComponent> extractKnowledgeComponents(Element parent) 
            throws NonRecoverableException {
        
        ArrayList<KnowledgeComponent> outcomes = new ArrayList<>();
        
        NodeList nodes = parent.getElementsByTagName("KnowledgeComponent");

        for (int i = 0; i < nodes.getLength(); i++)
            outcomes.add(extractKnowledgeComponent((Element) nodes.item(i)));
       
        return outcomes;
    }

    /**
     * Extract and return the knowledge component outcome the given
     * &lt;KnowledgeComponent> element.
     * 
     * @param element a &lt;KnowledgeComponent> element
     * @return a KnowledgeComponent
     * @throws NonRecoverableException 
     */
    private KnowledgeComponent extractKnowledgeComponent(Element element) throws NonRecoverableException {
        KnowledgeComponent outcome = new KnowledgeComponent(XmlMgr.getIntAttribute(element, "id"));

        outcome.setTitle(XmlMgr.getAttribute(element, "title"));

        outcome.setBloomLevel(extractBloomLevel(element));
        
        if (XmlMgr.getAttribute(element, "focus").equals("Tutor")) {
            outcome.setIsDomainFocus(false); // teaching the tutor/GUI itself
        } else {
            outcome.setIsDomainFocus(true);  // teaching SHA-256 domain
        }
        
        outcome.setExercisingLocations(extractExercisingLocations(element));
        
        return outcome;
    }

    /**
     * Extract and return the units in this course from the &lt;Unit> elements.
     * 
     * @param parent a &lt;Course> element
     * @return a List of Units
     * @throws NonRecoverableException 
     */
    private ArrayList<Unit> extractUnits(Element parent) throws NonRecoverableException {
        ArrayList<Unit> units = new ArrayList<>();

        int id = -1; // Declare here for better error reporting below

        NodeList nodes = parent.getElementsByTagName("Unit");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);

            id = XmlMgr.getIntAttribute(element, "id");

            Unit unit = new Unit(id);

            unit.setSequenceId(XmlMgr.getIntAttribute(element, "sequence"));

            unit.setTitle(XmlMgr.getAttribute(element, "title"));

            unit.setDescription(XmlMgr.contentText(element, "Description"));
            
            unit.setTasks(extractTasks(element));

            units.add(unit);
        }

        return units;

    }

    /**
     * Extract and create a problem outcome from the given element.
     *
     * @param element an &lt;Outcome> with type "Problem"
     * @return
     * @throws XmlException
     */
   /* private Outcome extractProblem(Element element) throws XmlException {
        int id = XmlMgr.getIntAttribute(element, "id");

        ProblemOutcome outcome = new ProblemOutcome(id);

        outcome.setSequenceId(XmlMgr.getIntAttribute(element, "sequenceId"));
        outcome.setProblemId(XmlMgr.getIntAttribute(element, "problemId"));

        return outcome;
    }
    */

    /**
     * Extract and return the tasks in the given &lt;Unit> element
     * 
     * @param parent &lt;Unit>   // ToDo is this always true?
     * @return a Task list.
     */
    private ArrayList<Task> extractTasks(Element parent) {
        ArrayList<Task> tasks = new ArrayList<>();
        
        for (Element child : XmlMgr.getChildren(parent, "Task"))
            tasks.add(extractTask(child));
        
        // Sort tasks by sequence order
        /*
        for (int i = 0; i < tasks.size() - 1; i++) {
            int minIdx = i;
            int minSeq = tasks.get(i).getSequenceId();
                
            for (int j = i + 1; j < tasks.size(); j++)   
                if (tasks.get(j).getSequenceId() < minSeq)
                    minIdx = j;
        
                
            if (i != minIdx) { // swap
                Task tmp = tasks.get(i);
                tasks.set(i, tasks.get(minIdx));
                tasks.set(minIdx, tmp);
            }
        }
        */
            
        return tasks;
    }
     
    /**
     * Extract and return the task form the given &lt;Task> XML element.
     * @param element a &lt;Task> XML element
     * @return a Task
     */
    private Task extractTask(Element element) {
        Task task = new Task(XmlMgr.getIntAttribute(element, "id"));
        task.setTitle(XmlMgr.getAttribute(element, "title"));
        task.setDescription(XmlMgr.contentText(element, "Description"));
        
        task.setSteps(extractSteps(element));
        
        NodeList nodes = element.getElementsByTagName("ExcercisedComponent");
            for (int i = 0; i < nodes.getLength(); i++)
                task.addExercisedComponentId(XmlMgr.getIntAttribute(((Element) nodes.item(i)), "componentId"));
     
        return task;
    }
    
    /**
     * Extract and return a list of child steps from the given parent element.
     * 
     * @param parent a parent XML element containing child &\lt<step type="...">
     *                 elements.
     * @return a List of Step elements, may be empty.
     */
    private ArrayList<Step> extractSteps(Element parent) {
        ArrayList<Step> steps = new ArrayList<>();
            
        for (Element child : XmlMgr.getChildren(parent, "Step")) 
             steps.add(extractStep(child));
        
        return steps;
    }
    
    /**
     * Extract and return a Step from the given &lt;Step> element.
     * 
     * @param element a &lt;Step> element
     * @return a Step 
     */
    private Step extractStep(Element element) {
            Step step = new Step(XmlMgr.getIntAttribute(element, "id"),
                                 XmlMgr.getIntAttribute(element, "sequence"),
                                 extractStepType(element));
         
            step.setTitle(XmlMgr.getAttribute(element, "title"));       
            step.setScaffolding(extractScaffolding(element));
            step.setTimeout(extractTimeout(element));
            step.setNotifyTutor(XmlMgr.getBooleanAttribute(element, "notifyTutor"));
            
            NodeList nodes = element.getElementsByTagName("ExcercisedComponent");
            for (int i = 0; i < nodes.getLength(); i++)
                step.addExercisedComponentId(XmlMgr.getIntAttribute(((Element) nodes.item(i)), "componentId"));
            
            for (Hint hint : extractHints(element))
                step.addHint(hint);

            switch (step.getSubType()) {
                case INFO_MESSAGE:
                    String msg = XmlMgr.contentText(element, "Msg");
                    InformationStep subStep = new InformationStep();
                    subStep.setMsg(msg);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    step.setData(gson.toJson(subStep));
                    break;
            }
            
           return step;
    }
    
    /**
     * Extract child &lt;ExercisingLocation> elements from given XML DOM parent 
     * element and return the list of these locations.
     *
     * @param parent an XML DOM Element containing one or more child
     * &lt;ExercisingLocation> node elements.
     * @throws ShaTuException a nonrecoverable exception also see getCause()
     * @return a List of ExercisingElements
     */
    private ArrayList<ExercisingLocation> extractExercisingLocations(Element parent) 
            throws NonRecoverableException {
        
        ArrayList<ExercisingLocation> locations = new ArrayList<>();
        
        NodeList nodes = parent.getElementsByTagName("ExercisingLocation");

        for (int i = 0; i < nodes.getLength(); i++)
            locations.add(extractExercisingLocation((Element) nodes.item(i)));
       
        return locations;
    }
    
    /**
     * Extract and return the exercising location from the given 
     * &lt;ExercisingLocation> element
     * 
     * @param element an &lt;ExercisingLocation> XML element
     * @return an ExercisingLocation
     * @throws NonRecoverableException 
     */
    private ExercisingLocation extractExercisingLocation(Element element) throws NonRecoverableException {
        ExercisingLocation location = new ExercisingLocation();

        location.setCourseId(XmlMgr.getIntAttribute(element, "course"));
        location.setUnitId(XmlMgr.getIntAttribute(element, "unit"));
        location.setTaskId(XmlMgr.getIntAttribute(element, "task"));
        location.setStepId(XmlMgr.getIntAttribute(element, "step"));
        
        return location;
    }
    
    /**
     * Extract and return the BloomLevel specified in the corresponding
     * XML attribute of the given element.
     * 
     * @param element an XML element containing a 'bloomLevel' attribute.
     * @return a BloomLevel enum value.
     */
    private BloomLevel extractBloomLevel(Element element) {
        switch (XmlMgr.getAttribute(element, "bloomLevel")) {
   
            case "Knowledge":
                return BloomLevel.KNOWLEDGE;
            case "Comprehension":
                return BloomLevel.COMPREHENSION;
            case "Application":
                return BloomLevel.APPLICATION;
            case "Analysis":
                return BloomLevel.ANALYSIS;
            case "Synthesis":
                return BloomLevel.SYNTHESIS;
            default:
                return BloomLevel.EVALUATION;
        }
    }
    
    /**
     * Extract the type from the given XML step element and return the
     * associated step subtype enum value.
     * 
     * @param stepElement &\lt<step type="..."> XML element.
     * @return a StepSubType
     */
    private StepSubType extractStepType(Element stepElement) {
        String stepType = XmlMgr.getAttribute(stepElement, "type");
        
        switch (stepType) {
            case "Information Message":
                return StepSubType.INFO_MESSAGE;
                
            default:
                String msg = "Unknown step type in CourseDAO: " + stepType; 
                Logger.getLogger(CourseDAO.class.getName()).log(Level.WARNING, msg);
                return StepSubType.DEFAULT;
        }
    }
    
    /**
     * Extract the scaffolding from the given XML task or step element and
     * return the associated scaffold level enum value.
     * 
     * @param element &\lt<task scaffolding="..."> &\lt<step scaffolding="...">
     *         XML element.
     * @return a ScaffoldLevel
     */
    private ScaffoldLevel extractScaffolding(Element element) {
        String scaffolding = XmlMgr.getAttribute(element, "scaffolding");
        
        switch (scaffolding) {
            case "None":
                return ScaffoldLevel.NONE;  
            case "Low":
                return ScaffoldLevel.LOW;
            case "Medium":
                return ScaffoldLevel.MEDIUM;
            case "High":
                return ScaffoldLevel.HIGH;
            case "Extreme":
                return ScaffoldLevel.EXTREME;
                
            default:
                String msg = "Unknown step scaffoling in CourseDAO: " + scaffolding; 
                Logger.getLogger(CourseDAO.class.getName()).log(Level.WARNING, msg);
                return ScaffoldLevel.NONE;
        }
    }
    
    /**
     * Extract a GUI gesture from the given element.
     * 
     * @param element an
     * @return GuiGesture
     */
    private GuiGesture extractGesture(Element element) {
        String gesture = XmlMgr.getAttribute(element, "gesture");
        
        switch(gesture) {
            case "Request Hint":
                return GuiGesture.REQUEST_HINT;
        
            default:
                String msg = "Unknown step gesture in CourseDAO: " + gesture; 
                Logger.getLogger(CourseDAO.class.getName()).log(Level.WARNING, msg);
                return GuiGesture.NO_OP;
        }
    }
    
    /**
     * Extract and return a list of hints from the given parent element.
     * 
     * @param parent an Element possibly containing child &lt;Hint> elements.
     * @return a List&lt;Hint>
     */
    private ArrayList<Hint> extractHints(Element parent) {
        ArrayList<Hint> hints = new ArrayList<>();

        for (Element child : XmlMgr.getChildren(parent, "Hint")) {
            Hint hint = new Hint(XmlMgr.getIntAttribute(child, "id"));
            hint.setSequenceId(XmlMgr.getIntAttribute(child, "sequence"));
            
            hint.setText(XmlMgr.getContentText(child));
            
            hints.add(hint);
        }
        
        return hints;
    }
    
    /**
     * Extract and return the task selection from the given &lt;Course> element.
     * 
     * @param element a &lt;Course>
     * @return a TaskSelectionKind
     */
    private TaskSelectionKind extractPrimaryPedagogy(Element element) {
        String pedagogy = XmlMgr.getAttribute(element, "primaryPedagogy");
        
        String errMsg = "";
        
        switch (pedagogy) {
            case "Student Choice":
                return TaskSelectionKind.STUDENT_CHOICE;
                
            case "Fixed Sequence":
                return TaskSelectionKind.FIXED_SEQUENCE;
                
            case "Mastery Learning":
                return TaskSelectionKind.MASTERY_LEARNING;
               
            case "Microadaptation":
                return TaskSelectionKind.MICROADAPTATION;
                
            case "Other":
                // OTHER is used in an KnowledgeComponent Outcome to indicate
                // the task associated with the outcome is selected via another
                // strategy, such as Fixed Sequence in a Unit. Hence, OTHER
                // is not a legal value for an entire course. If an Outcome of
                // a course is used to trigger task select, then the pedagogy
                // should be MICROADAPTATION.
                errMsg = "Illegal pedagogy in CourseDAO (see Note): " + pedagogy; 
                Logger.getLogger(CourseDAO.class.getName()).log(Level.WARNING, errMsg);
                return TaskSelectionKind.OTHER;
           
            default:
                errMsg = "Unknown pedagogy in CourseDAO: " + pedagogy; 
                Logger.getLogger(CourseDAO.class.getName()).log(Level.WARNING,errMsg);
                return TaskSelectionKind.ERROR;
        }
    }
    
    /**
     * Extract and return, if any, the child &lt;Timeout> element contained
     * within the given parent XML element
     * 
     * @param parent
     * @return a Timeout or null
     */
    private Timeout extractTimeout(Element parent) {
        Element element = XmlMgr.getChild(parent, "Timeout");
        
        if (element == null) {
            return null;
        } else {
 
            return new Timeout(XmlMgr.getAttribute(element, "type"),
                               XmlMgr.getIntAttribute(element, "seconds"),
                               XmlMgr.getAttribute(element, "event"),
                               XmlMgr.getAttribute(element, "text"));
        }  
    }
}

