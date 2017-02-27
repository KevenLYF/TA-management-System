<?php
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/Applicant.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/Application.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/Assignment.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/Course.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/Department.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/GraderJob.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/Hours.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/Instructor.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/Job.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/ResourceManager.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/TAjob.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/model/User.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/controller/InputValidator.php';
require_once '/Users/KevenLiu/Documents/workspace/TAMAS_Web/Group12/WebTAMAS/persistence/Persistence.php';

class Controller
{
	public function __construct(){
		
	}
	
	public function register($userName, $userID){
		$name = InputValidator::validate_input($userName);
		$ID = InputValidator::validate_input($userID);
		$error = "";
		
		if($name == null || strlen($name) == 0){
			$error .= "@1User name cannot be empty! ";
		
		}
	    if(strlen($ID) != 9 || ctype_digit($ID) == FALSE){
			$error .= "@2User ID must be 9 digits! ";

		}
		if(ctype_digit($name) == TRUE){
			$error .= "@1User name must contain letters!";
		}
		
		if(strlen(trim($error)) == 0){
			$ps = new Persistence();
			$rm = $ps->loadDataFromStore();
			
			$instructor = new Instructor($name, $ID);
			$rm->addInstructor($instructor);
			
			$ps->writeDataToStore($rm);						
		}
		else{
			throw new Exception(trim($error));
		}
		
	}
	
	public function login($userName, $userID){
		$name = InputValidator::validate_input($userName);
		$ID = InputValidator::validate_input($userID);
		$error = "";
		
		$tempInstructor = new Instructor($name, $ID);
		
		$ps = new Persistence();
		$rm = $ps->loadDataFromStore();
		

		
		for($i = 0; $i < $rm->numberOfInstructors(); $i++){
		if($rm->getInstructor_index($i)->equals($tempInstructor)){
				
				return $i;
				
			}
		}
		$error = "User not found!";
		throw new Exception(trim($error));
				
	}
	
	public function loadCourse(){
		$ps = new Persistence();
		$rm = $ps->loadDataFromStore();
	
		
		if($rm->numberOfCourses() == 0){
			$ECSE321 = new Course("ECSE321", 2, 0, 200);
			$ECSE421 = new Course("ECSE421", 1, 2, 100);
			$ECSE521 = NEW Course("ECSE521", 1, 4, 60);
			$ECSE600 = new Course("ECSE600", 1, 4, 40);
			$COMP350 = new Course("COMP350", 2, 0, 150);
			$FACC200 = new Course("FACC200", 0, 0, 300);
			
			$rm->addCourse($ECSE321);
			$rm->addCourse($ECSE421);
			$rm->addCourse($ECSE521);
			$rm->addCourse($ECSE600);
			$rm->addCourse($COMP350);		
			$rm->addCourse($FACC200);
		}
		$ps->writeDataToStore($rm);
	}
	
	public function addCourse(Course $course){
		
		$ps = new Persistence();
		$rm = $ps->loadDataFromStore();
		$rm->addCourse($course);		
		$ps->writeDataToStore($rm);
		
	}
	
	public function createTAJob($aMaxHours, $aWage, $aDeadline, $aRequiredSkills, $aRequiredCourseGPA, $aRequiredCGPA, $aRequiredExperience, $aCourse, $aMinHours, $aIsLab){
		$maxHour = InputValidator::validate_input($aMaxHours);
		$wage = InputValidator::validate_input($aWage);
		$deadline = InputValidator::validate_input($aDeadline);
		$courseGPA = InputValidator::validate_input($aRequiredCourseGPA);
		$cGPA = InputValidator::validate_input($aRequiredCGPA);
		$skills = InputValidator::validate_input($aRequiredSkills);
		$experience = InputValidator::validate_input($aRequiredExperience);
		
		$error = "";
		
		if(ctype_digit($maxHour) == FALSE || $maxHour > 180){
			$error .= "@1Max hour must be digits and smaller than 180! ";
		}
		if(ctype_digit($wage) == FALSE){
			$error .= "@2Wage per hour must be digits! ";
		}
		if(!strtotime($deadline)){
			$error .= "@3Deadline date must be specified correctly (YYYY-MM-DD)! ";
		}
		if(ctype_digit($courseGPA[0]) == FALSE || $courseGPA[1] != "." || ctype_digit(substr($courseGPA, 2)) == FALSE){
			$error .= "@4GPA must be specified correctly (x.xx)! ";
		}
		if(ctype_digit($cGPA[0]) == FALSE || $cGPA[1] != "." || ctype_digit(substr($cGPA, 2)) == FALSE){
			$error .= "@5GPA must be specified correctly (x.xx)! ";
		}
		
		if(strlen(trim($error)) == 0){
		$ps = new Persistence();
		$rm = $ps->loadDataFromStore();
		
		$taJob = new TAjob($aMaxHours, $aWage, $aDeadline, $aRequiredSkills, $aRequiredCourseGPA, $aRequiredCGPA, $aRequiredExperience, $aCourse, $aMinHours, $aIsLab);
		$rm->addJob($taJob);
		
		$ps->writeDataToStore($rm);
		}
		else{
			throw new Exception(trim($error));
		}
	}
	
	public  function createGraderJob($aMaxHours, $aWage, $aDeadline, $aRequiredSkills, $aRequiredCourseGPA, $aRequiredCGPA, $aRequiredExperience, $aCourse){
		$maxHour = InputValidator::validate_input($aMaxHours);
		$wage = InputValidator::validate_input($aWage);
		$deadline = InputValidator::validate_input($aDeadline);
		$courseGPA = InputValidator::validate_input($aRequiredCourseGPA);
		$cGPA = InputValidator::validate_input($aRequiredCGPA);
		$skills = InputValidator::validate_input($aRequiredSkills);
		$experience = InputValidator::validate_input($aRequiredExperience);
		
		$error = "";
		
		if(ctype_digit($maxHour) == FALSE || $maxHour > 180){
			$error .= "@1Max hour must be digits and smaller than 180! ";
		}
		if(ctype_digit($wage) == FALSE){
			$error .= "@2Wage per hour must be digits! ";
		}
		if(!strtotime($deadline)){
			$error .= "@3Deadline date must be specified correctly (YYYY-MM-DD)! ";
		}
		if(ctype_digit($courseGPA[0]) == FALSE || $courseGPA[1] != "." || ctype_digit(substr($courseGPA, 2)) == FALSE){
			$error .= "@4GPA must be specified correctly (x.xx)! ";
		}
		if(ctype_digit($cGPA[0]) == FALSE || $cGPA[1] != "." || ctype_digit(substr($cGPA, 2)) == FALSE){
			$error .= "@5GPA must be specified correctly (x.xx)! ";
		}
		
		
		
		if(strlen(trim($error)) == 0){
		$ps = new Persistence();
		$rm = $ps->loadDataFromStore();
		
		$graderJob = new GraderJob($aMaxHours, $aWage, $aDeadline, $aRequiredSkills, $aRequiredCourseGPA, $aRequiredCGPA, $aRequiredExperience, $aCourse);
		$rm->addJob($graderJob);
		
		$ps->writeDataToStore($rm);
		}
		else{
			throw new Exception(trim($error));
		}
	}
}

?>
