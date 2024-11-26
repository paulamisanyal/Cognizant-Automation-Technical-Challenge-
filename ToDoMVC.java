package NewPackage;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ToDoMVC {

	WebDriver driver;

	//Browser invoke and get url.
	@BeforeTest
	public void openToDo()
	{
		System.setProperty("webdriver.chrome.driver", "C:\\Personal\\Paulami\\Selenium\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://todomvc.com/examples/react/dist/");
		driver.manage().window().maximize();
	}
	
	@AfterTest
	public void closeToDo() {
		driver.quit();
	}

	// Scenario 1: Adding a new todo item
	@Test
	public void addTodos() {  

		String[] tasks = {"Breakfast", "Lunch", "Dinner","Fetch children","Cleaning house","Cook","Exercise"};

		for (String task : tasks) {
			driver.findElement(By.className("new-todo")).sendKeys(task, Keys.ENTER);
		}

		List<WebElement> todoItems = driver.findElements(By.cssSelector(".todo-list li"));

		// Assert the size of the list matches the expected number of tasks
		Assert.assertEquals(todoItems.size(), tasks.length, 
				"The number of todo items on the list matches the expected count");

		// Print all items for verification
		for (int i = 0; i < todoItems.size(); i++) {
			System.out.println("Todo Item " + (i + 1) + ": " + todoItems.get(i).getText());
		}                  
	}

	//Scenario 2: Marking a todo as completed
	@Test
	public void completedTodo() throws InterruptedException {
		driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).click();
		driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).click();
		Thread.sleep(2000);
	}

	//Scenario 3: Unmarking a completed todo
	@Test
	public void unmarkingCompletedTodos() throws InterruptedException {
		driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).click();
		Thread.sleep(2000);
	}

		//Scenario 4: Editing an existing todo
	@Test
    public void editExistingTodo() {
		
	    // Locate the todo item to be edited
		WebElement todoLabel = driver.findElement(By.xpath("//label[text()='Cleaning house']"));
		
		// Double-click the todo item to make it editable
	    Actions actions = new Actions(driver);
	    actions.doubleClick(todoLabel).perform();
		//Assert.assertEquals(todoLabel.getText(), originalTodo, "The todo item was added correctly");
	    
	 // Re-locate the input field in case the DOM changes
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    WebElement editInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".todo-list li.editing .edit")));
	    System.out.println("Edit input field is now visible.");
	    
	 // Update the text in the input field and save changes
	    String updatedText = "Cleaning garden";
	    editInputField.clear();
	    editInputField.sendKeys(updatedText);
	    editInputField.sendKeys(Keys.ENTER); // Save the updated text by pressing "Enter"
	    
	    }

	//Scenario 5: Deleting a todo ite
	@Test
	public void deleteExistingTodos() throws InterruptedException {

		// Locate the 6th todo item in the list
		WebElement sixthTodo = driver.findElement(By.cssSelector(".todo-list li:nth-child(6)"));

		// Hover over the 6th todo item to reveal the delete button
		Actions actions = new Actions(driver);
		actions.moveToElement(sixthTodo).perform();

		// Locate and click the delete button for the 6th todo item
		WebElement deleteButton = sixthTodo.findElement(By.cssSelector(".destroy"));
		deleteButton.click();

		// Verify the 6th todo item has been deleted
		List<WebElement> todos = driver.findElements(By.cssSelector(".todo-list li"));
		Assert.assertTrue(todos.size() < 7, "The 7th todo item was deleted");

		System.out.println("Test Delete 6th Todo: Passed");
		Thread.sleep(5000);
	}
	
	//Scenario 6: Viewing all todos
	@Test
	public void viewAllTodos() {

		driver.findElement(By.xpath("//a[normalize-space()='All']")).click();

		List<WebElement> displayedTodos = driver.findElements(By.cssSelector(".todo-list li"));
		Assert.assertEquals(displayedTodos.size(), 6, "Incorrect number of todos displayed in 'All' filter!");

		String[] expectedTodos = {"Breakfast", "Lunch", "Dinner","Fetch children","Cleaning house","Exercise"};
		for (int i = 0; i < displayedTodos.size(); i++) {
			String todoText = displayedTodos.get(i).findElement(By.tagName("label")).getText();
			Assert.assertEquals(todoText, expectedTodos[i], "Todo text does match");
			System.out.println("Displayed Todo: " + todoText);
		}

		System.out.println("Test View All Todos: Passed");
	}

	//Scenario 7: Viewing active todos
	@Test
	public void viewActiveTodos() throws InterruptedException {
		driver.findElement(By.xpath("//a[normalize-space()='Active']")).click();

		//Verify only active todo items are displayed
		List<WebElement> displayedTodos = driver.findElements(By.cssSelector(".todo-list li"));
		Assert.assertEquals(displayedTodos.size(), 5, "Correct number of active todos displayed");

		for (WebElement todo : displayedTodos) {
			String todoText = todo.findElement(By.tagName("label")).getText();
			Assert.assertFalse(todo.getAttribute("class").contains("completed"), 
					"Completed todo should not be displayed in the active filter");
			System.out.println("Displayed Active Todo: " + todoText);
		}

		System.out.println("Test View Active Todos: Passed");

		Thread.sleep(4000);
	}


	//Scenario 8: Viewing completed todos
	@Test
	public void viewCompletedTodos() throws InterruptedException {
		driver.findElement(By.xpath("//a[normalize-space()='Completed']")).click();

		// Verify only completed todos are displayed
		List<WebElement> displayedTodos = driver.findElements(By.cssSelector(".todo-list li"));
		Assert.assertEquals(displayedTodos.size(), 1, "Correct number of completed todos displayed!");

		for (WebElement todo : displayedTodos) {
			String todoText = todo.findElement(By.tagName("label")).getText();
			Assert.assertTrue(todo.getAttribute("class").contains("completed"), 
					"Active todo should not be displayed in the completed filter");
			System.out.println("Displayed Completed Todo: " + todoText);
		}

		System.out.println("Test View Completed Todos: Passed");
		Thread.sleep(3000);
	}

	//Scenario 9: Displaying the number of remaining tasks
	@Test
	public void viewRemainingTodos() throws InterruptedException {

		driver.findElement(By.xpath("//a[normalize-space()='Active']")).click();
		// Locate all todo items in the list
		List<WebElement> todoItems = driver.findElements(By.cssSelector(".todo-list li"));

		// Filter out the active todos (not marked as completed)
		int activeTodosCount = 0;
		for (WebElement todo : todoItems) {
			String todoClass = todo.getAttribute("class");
			if (!todoClass.contains("completed")) { // Check if the item is not marked as completed
				activeTodosCount++;
			}
		}

		// Display the count of active todos
		System.out.println("Number of active todos: " + activeTodosCount);

		// Verify the count against the footer text
		WebElement footer = driver.findElement(By.className("footer"));
		String footerText = footer.findElement(By.cssSelector(".todo-count")).getText();
		int displayedCount = Integer.parseInt(footerText.split(" ")[0]); // Extract the number from the text

		Assert.assertEquals(activeTodosCount, displayedCount, 
				"The count of active todos match the number displayed in the footer");

		System.out.println("Test Capture Active Todos Count: Passed");
		Thread.sleep(3000);
	}
	
	//Scenario 10: Clearing all completed todos
		@Test
		public void clearAllCompletedTodos() {
			
			// WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			   // WebElement clearCompletedButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".clear-completed")));
			
			    // Click the "Clear completed" button
		    driver.findElement(By.cssSelector(".clear-completed")).click();

		    // Verify that all completed todos are removed
		    List<WebElement> remainingTodos = driver.findElements(By.cssSelector(".todo-list li"));
		    for (WebElement todo : remainingTodos) {
		        String todoClass = todo.getAttribute("class");
		        Assert.assertFalse(todoClass.contains("completed"), "A completed todo was cleared");
		    }
		}
	
}



