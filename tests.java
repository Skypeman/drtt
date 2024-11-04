public static void test1() {
    WebDriver driver = new ChromeDriver();
    final String URL = "http://auto.drom.ru/";
    String model = "Harrier";
    String year = "2007";
    String expectedBackground = "0px 2px";

    driver.get(URL);

    WebElement advancedFilterButton = driver.findElement(By.xpath("//button[@data-ftid=\"sales__filter_advanced-button\"]"));
    advancedFilterButton.click();

    WebElement markSelect = driver.findElement(By.xpath("//input[@role = 'combobox' and @placeholder='Марка']"));
    WebElement toyotaSelector = driver.findElement(By.xpath("//div[@role = 'option' and contains(text(), 'Toyota')]"));
    WebElement fuelSelect = driver.findElement(By.xpath("//button[contains(text(), 'Топливо')]"));
    WebElement hybridSelector = driver.findElement(By.xpath("//div[@role = 'option' and contains(text(), 'Гибрид')]"));
    WebElement unsoldCheckbox = (new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//label[@for=\"sales__filter_unsold\"]"))))
    );
    WebElement mileageFromInput = driver.findElement(By.xpath("//input[@placeholder='от, км']"));
    WebElement yearFromSelect = driver.findElement(By.xpath("//button[text()=\"Год от\"]"));
    WebElement filterSubmitButton = driver.findElement(By.xpath("//button[@data-ftid=\"sales__filter_submit-button\"]"));

    markSelect.click();
    toyotaSelector.click();

    WebElement modelSelect = (new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//input[@role = 'combobox' and @placeholder='Модель']"))))
    );
    modelSelect.click();
    modelSelect.sendKeys(model);

    WebElement harrierSelector = (new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[@role = 'option' and contains(text(), '%s')]", model))))
    );
    harrierSelector.click();

    fuelSelect.click();
    hybridSelector.click();

    unsoldCheckbox.click();

    mileageFromInput.click();
    mileageFromInput.sendKeys("1");

    yearFromSelect.click();
    WebElement yearFromSelector = driver.findElement(By.xpath(String.format("//div[@role = 'option' and contains(text(), '%s')]", year)));
    yearFromSelector.click();

    filterSubmitButton.click();

    List<WebElement> bullsNameList = driver.findElements(By.xpath("//div[@data-bulletin-list=\"true\"]/div[@data-ftid=\"bulls-list_bull\"]/div[2]/div[1]/a/h3"));
    List<WebElement> bullsMileagesList = driver.findElements(By.xpath("//div[@data-bulletin-list=\"true\"]/div[@data-ftid=\"bulls-list_bull\"]/div[2]/div[2]/span[last()]"));

    for (WebElement bullName : bullsNameList) {
        String actualBackground = bullName.getCssValue("background-size");
        Assert.assertEquals(actualBackground, expectedBackground);
    }

    for (WebElement bullMileage : bullsMileagesList) {
        String actualMileage = bullMileage.getText();
        String actualMileageEnd = actualMileage.substring(actualMileage.length() - 2);
        Assert.assertEquals(actualMileageEnd, "км");
    }

    WebElement secondPageButton = driver.findElement(By.xpath("//a[@data-ftid=\"component_pagination-item\" and text()='2']"));
    secondPageButton.click();

    bullsNameList = driver.findElements(By.xpath("//div[@data-bulletin-list=\"true\"]/div[@data-ftid=\"bulls-list_bull\"]/div[2]/div[1]/a/h3"));
    bullsMileagesList = driver.findElements(By.xpath("//div[@data-bulletin-list=\"true\"]/div[@data-ftid=\"bulls-list_bull\"]/div[2]/div[2]/span[last()]"));

    for (WebElement bullName : bullsNameList) {
        String actualBackground = bullName.getCssValue("background-size");
        Assert.assertEquals(actualBackground, expectedBackground);
    }

    for (WebElement bullMileage : bullsMileagesList) {
        String actualMileage = bullMileage.getText();
        String actualMileageEnd = actualMileage.substring(actualMileage.length() - 2);
        Assert.assertEquals(actualMileageEnd, "км");
    }

    driver.quit();
}

public static void test2() {
    WebDriver driver = new ChromeDriver();
    final String URL = "http://auto.drom.ru/";

    driver.get(URL);
    String expectedNotificationInnerText = "Объявление добавлено в избранное.\nВы получите уведомление при изменении цены.";

    WebElement loginButton = driver.findElement(By.xpath("//a[@data-ftid=\"component_header_login\"]"));
    loginButton.click();

    WebElement loginInput = driver.findElement(By.id("sign"));
    loginInput.click();
    loginInput.sendKeys(LOGIN);

    WebElement passwordInput = driver.findElement(By.id("password"));
    passwordInput.click();
    passwordInput.sendKeys(PASSWORD);

    WebElement signinButton = driver.findElement(By.id("signbutton"));
    signinButton.click();

    List<WebElement> favoriteButtons = driver.findElements(By.xpath("//button[@data-ftid=\"component_favorite_button_add\"]"));

    int indexOfRandomBull = (int) (Math.random() * (favoriteButtons.size() - 1));
    favoriteButtons.get(indexOfRandomBull).click();

    WebElement notificationPopup = (new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-ftid=\"component_notification_type_success\"]")))
    );
    String actualNotificationInnerText = notificationPopup.getAttribute("innerText");

    Assert.assertEquals(actualNotificationInnerText, expectedNotificationInnerText);

    driver.quit();
}

public static void test3() {
    WebDriver driver = new ChromeDriver();
    final String URL = "http://auto.drom.ru/";

    driver.get(URL);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

    WebElement regionButton = driver.findElement(By.xpath("//a[@data-ftid=\"component_header_region\"]"));
    regionButton.click();

    WebElement primorskiyKrayLink = driver.findElement(By.xpath("//a[text()=\"Приморский край\"]"));
    primorskiyKrayLink.click();

    WebElement markSelect = driver.findElement(By.xpath("//input[@role = 'combobox' and @placeholder='Марка']"));
    markSelect.click();

    List<WebElement> markList = new ArrayList<>();
    HashMap<String, Integer> marksAndAds = new HashMap<>();
    String markName = ""; String adsCount = "";

    WebElement markDropdownDiv = driver.findElement(By.xpath("//div[@aria-label=\"Марка\"]//div[@role=\"listbox\"]/div"));
    String markDropdownHeight = markDropdownDiv.getCssValue("height");
    int markDropdownHeightValue = Integer.parseInt(markDropdownHeight.substring(0, markDropdownHeight.length() - 2));
    int markListLastY;
    String markListLastStyle;
    do {
        markList.clear();
        markList.addAll(driver.findElements(By.xpath("//div[@aria-label=\"Марка\"]//div[@role=\"option\"]")));
        for (WebElement mark : markList) {
            if (mark.isDisplayed()) {
                markName = mark.getText().replaceAll("[^A-Za-z]+", "");
                adsCount = mark.getText().replaceAll("\\D+", "");
                if (!adsCount.isEmpty()) {
                    marksAndAds.put(markName, Integer.parseInt(adsCount));
                }
            }
        }
        markListLastStyle = markList.getLast().findElement(By.xpath("..")).getAttribute("style");
        markListLastY = Integer.parseInt(markListLastStyle.substring(36, markListLastStyle.length() - 4));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", markList.getLast());
    }
    while (markListLastY < (markDropdownHeightValue - 35));

    Map<String, Integer> sortedMarksAndAds = marksAndAds.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new))
            .reversed();

    Iterator<Map.Entry<String, Integer>> iterator = sortedMarksAndAds.entrySet().iterator();
    System.out.println("| Фирма | Количество объявлений |");
    for (int i = 0; i < 20; i++) {
        Map.Entry<String, Integer> tmp = iterator.next();
        System.out.println("| " + tmp.getKey() + " | " + tmp.getValue() + " |");
    }

    driver.quit();
}
