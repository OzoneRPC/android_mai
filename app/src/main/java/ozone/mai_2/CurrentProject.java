package ozone.mai_2;

/**
 * Created by Ozone on 28.06.2016.
 */
public class CurrentProject {

    private static Project project;

    public static Project getCurrentProject(){
        return project;
    }
    public static void setCurrentProject(Project newProject){
        project = newProject;
    }
}
