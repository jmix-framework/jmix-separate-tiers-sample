# Jmix Separate Tiers Sample

This project demonstrates how to separate a Jmix application to the frontend and backend tiers using the [REST API](https://docs.jmix.io/jmix/rest) and [REST DataStore](https://docs.jmix.io/jmix/rest-ds) add-ons.

The resulting distributed system replicates the functionality of the [Onboarding]() project and consists of two web applications:

- Backend: contains JPA entities stored in the database. Exposes the data model through the generic REST API. Includes Data Tools and Audit add-ons for data administration.
- Frontend: contains DTO entities identical by structure to the backend JPA entities. Performs CRUD operations using the REST data store.

The Frontend application can be deployed in a DMZ and scaled differently from the Backend.

Both applications are connected to their own databases, but the Frontend database stores only user settings. All application data is stored in the Backend database. 

Both applications have user interface, but the Backend provides only the user management views, Entity Log and Entity Inspector. The Frontend provides the entire user interface for the Onboarding functionality.

## Setup

The applications are configured to be run on different ports. To avoid clashing of session cookies between web applications, their host names must also be different. So it is assumed that the applications are available on the following URLs:

- Frontend: http://localhost:8080
- Backend: http://host1:8081

Add `host1` name pointing to localhost to your `hosts` file:

```
127.0.0.1       host1
```

Now you can run both applications on localhost from the IDE and access them using the URLs from above. 

## Running

Open the root project in IntelliJ with Jmix Studio plugin installed and use **Backend-app Jmix Application** and **Frontend-app Jmix Application** run/debug configurations to run the applications.

Open http://host1:8081 in your web browser and log in as _admin_ to the Backend application. 

Open **Users** from the main menu. Create a new user with _john_ username and select _Operations_ in the **Department** field. Save the user and assign the _Employee_ role to him.

Open http://localhost:8080 in another browser tab and log in as _james_ (with password _1_) to the Frontend application. James has the _HR Manager_ role, and he can manage users of his department.

Open **Users** from the main menu and edit the _john_ record. Set a value to the **Joining date** field and click **Generate**. The application will generate onboarding steps for this user.

Log out from the Frontend application, then log in as _john_. John has the _Employee_ role, so he can open **My onboarding** view and mark completed steps.  

Any user can log in to the Frontend application, but no one except _admin_ can enter the Backend application.

## Implementation Details

The Backend application includes Authorization Server and configures it for Resource Owner Password Credentials and Refresh Token grants, see its [application.properties](backend-app/src/main/resources/application.properties). 

The Frontend application shows its login view to the user, accepts a username and password and creates a `RestAuthenticationToken` to pass it to `AuthenticationManager` (see [RestLoginViewSupport](frontend-app/src/main/java/com/company/frontend/security/RestLoginViewSupport.java)). The REST DataStore add-on configures the `RestAuthenticationProvider` responding to `RestAuthenticationToken` because the Frontend application contains this property in [application.properties](frontend-app/src/main/resources/application.properties):

```properties
jmix.restds.authentication-provider-store = backend
```

`RestAuthenticationProvider` authenticates in the Backend's Authorization Server with the username and password, obtains access and refresh tokens and stores them in the user session.

After that, when invoking the Backend REST API, the REST DataStore gets the access token from the session and passes it with requests. When the access token expires, the REST DataStore obtains a new access token using the stored refresh token. If it fails, [InvalidRefreshTokenExceptionHandler](frontend-app/src/main/java/com/company/frontend/exception/InvalidRefreshTokenExceptionHandler.java) logs out the user and redirects them to the login view.  

The base `AbstractRestUserRepository` class in the Frontend application assigns to the user all roles defined in the Frontend that match by name to the user roles in the Backend application. For example, the user `bob` has the `employee` role assignment in the database. So when he logs in, both Backend and Frontend applications assign this role to him. However, the [Backend Employee](backend-app/src/main/java/com/company/backend/security/EmployeeRole.java) and [Frontend Employee](frontend-app/src/main/java/com/company/frontend/security/EmployeeRole.java) roles are different and define the user permissions in the respective tiers. 

The [HrManagerRlRole](backend-app/src/main/java/com/company/backend/security/HrManagerRlRole.java) in the Backend app restricts the list of users for HR Managers - they can see only users of their department. 

The Frontend views are mostly identical to the views of the basic Onboarding application, with the following differences:

- Instead of in-place fetch plans, the named fetch plans are defined in the [fetch-plans.xml](frontend-app/src/main/resources/com/company/frontend/fetch-plans.xml) repository. The same fetch plans are defined in the Backend application.
- [my-onboarding-view.xml](frontend-app/src/main/resources/com/company/frontend/view/myonboarding/my-onboarding-view.xml) defines the query in the JSON format of the REST API instead of JPQL. As this query doesn't support sorting, the sort order is defined additionally in the load delegate.