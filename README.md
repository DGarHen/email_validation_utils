# Email Validation Utils

This project contains a utility module designed to facilitate automated validation of email content, primarily through the Microsoft Graph API.

## 🧩 How to Use

To integrate this utility into your project, move the content of the `utils` package into the following path:

## src/main/utils
> ⚠️ Make sure to preserve the package structure to avoid compilation issues.

## 🔧 Graph API Configuration

By default, this utility uses the **Microsoft Graph API**. However, it can be adapted to other technologies if needed.

To configure the Graph API for your specific application, modify the following class:

## utils.emailApiUtils.GraphServiceClientInitializer
This class must be updated with the **Graph API keys** and client credentials relevant to your target application.

## 🧪 Example Step Usage

The file below contains an example of how to use the utility in step definitions:
utils/emailApiUtils/MicrosoftEmailSteps.java

This demonstrates common operations such as fetching recent emails, validating their content, and retrying until an email is received.

## 🧰 HTML Body Parsing

The following utility class is required for parsing email HTML content:
utils/HtmlUtils.java


This class helps extract specific elements from email bodies using XPath, which is essential for validating message content and structure.

## 🚀 Extensibility

Although currently coupled with the Microsoft Graph API, the architecture allows for easy substitution of email services by adapting the service-related classes.

---

Feel free to fork, contribute, or adapt the utility as needed for your testing framework!







