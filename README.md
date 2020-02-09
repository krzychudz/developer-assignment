# neos developer task 
1) Architecture:   
  I decided to use MVVM architecture pattern. I have chosen that one because in my opinion is sufficient to that kind of task.  Using MVVM gives me clean architecture and clean code because I could move buisness logic to view models. View is and should be responsible only for presenting results for user. I was thinking about using MVI but to be honest because of time I choose MVVM because I know that pattern better. MVVM gives us possibility to easy testing because we are able to easy create mocks of view models etc.  
 2) Project structure:   
      - DashboardActivity - main activity of application, using to dispaly list of comics and give a possibility to search for specific comics.  
       - DashboardViewModel - dashboard activity's view model, using to make all bussiness logic related to dashboard       activity:  
              1) Requesting data form backend,  
              2) Holding information about current pagination state (total item, data offset etc.).      
       - ApiClient - That class is responsible for handling backend communication. It is providing requests as observable objects using JavaRx.
       - ApiService - It is an interface to backend calls using retorfit2 library.
       - Models - Project also contains several models to make backend communication easier.
  3) Dependency injection: 
       - Application is using dependency injection. The library I used to do that is koin. In general DI in the project is responsible for:    
                    * Inject ApiCient class (by ApiClientInterface) to view models to give them ability to communication with backend.   
                    * Inject view models to activites/fragments using koin build-in mechanism.
  
  4) Views:  
       - As I mentioned dashboard activity is using only for displaying result for user. I tried to isolate evry other responsibilities in view models. Data flow in the application looks like:  
              1) I make a request, using view model to get comics.   
              2) When view model get an response then assign it to MutableLiveData.  
              3) Dashboard activity is observing the MutableLiveData so it can react asynchronously for the changes.  
              4) Dashboard can be in 3 states:  
                  - Fetching data ,   
                  - Data fetched with success,  
                  - Data fetch with failure .   
              I'm using a method in dashboard activity to render view depends on the state in which the activity is. 
  5) Searching:
        - For searching I just bind onChange event to EditText using RxBinding and on every change I'm making an request to backend. I set debounce on it to make a little less api calls and gives user time to put the whole comics title he want. 
  6) Improvements
        - I think it would be nice use cacheing to cache some of users find requests to execute it faster in the future. Also maybe using Android Paging Library for pagination instead of an old solution based on endless scroll listener would be nice. 
