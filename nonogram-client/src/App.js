import './App.css';
import Options from './components/Options';
import Describer from './components/Describer';
import Footer from './components/Footer';
import Header from './components/Header';
import Content from './components/Content';

import MENU_OPTIONS from './utils/menu';
import { useEffect } from 'react';
import { useState, useCallback } from 'react';
import Login from './components/Login';


const API_URL = 'http://127.0.0.1:8080/api/login';

function App() {


  const [player, setPlayer] = useState("guest")
  const [navigation, setNavigation] = useState(MENU_OPTIONS.LOGGIN)

  const [data, setData] = useState({nickname:'',password:''});
  const [status, setStatus] = useState("start");

  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  const changeNavigation = (nav)=>{
    setNavigation(nav)
    gameCountFetching();
  }


  const onCreateUser = useCallback(async (formData) => {
   
    setIsLoading(true);
    setError('');
    setPlayer(formData.nickname);

    try {
      const res = await fetch(API_URL+"/signup", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nickname: formData.nickname,
          password: formData.password
        }),
      });
      const gamedata = await res.json();
      

      setPlayer(gamedata.nickname)
      setStatus("ok");
    } catch (error) {
      setError(error.message);
    }
    setData({nickname:'',password:''});
    setNavigation(8);

  },[]);

  //api login
  const handleFormSubmit = useCallback(async (e,formData) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');
    setPlayer(formData.nickname);

    try {
      const res = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nickname: formData.nickname,
          password: formData.password
        }),
      });
      const gamedata = await res.json();
      

      if( gamedata.nickname==="wrong" ){

          setStatus(gamedata.nickname);

          setData({nickname:'',password:''});
          return (
            <div className="App" style={{backgroundColor:"#212121", width:"100vw"}}>
      
              <Login onCreateUser={onCreateUser} status= {gamedata.nickname} data={data} setData={setData} formSubmit={handleFormSubmit} />
              
              
            </div>
          );
      } else if( gamedata.nickname==="none"){

        setStatus(gamedata.nickname);

        return (
          <div className="App" style={{backgroundColor:"#212121", width:"100vw"}}>
    
            <Login onCreateUser={onCreateUser} status= {gamedata.nickname} data={data} setData={setData} formSubmit={handleFormSubmit} />
            
            
          </div>
        );
      }

      

      setPlayer(gamedata.nickname)
      setStatus("ok");
    } catch (error) {
      setError(error.message);
    }
   
    setIsLoading(false);
    setNavigation(0);
  }, []);


  const onExit = useCallback(async () =>{
    setData({nickname:'',password:''});
    setNavigation(8);
  },[])



  const [gamesCount, setGamesCount] = useState(-1);

  const gameCountFetching = async ()=>{

       
        async function fetchData(){
            try {
                const res = await fetch("http://127.0.0.1:8080/api/game/gamesCount?nickname="+player, {    
                  method: 'GET',     
                  crossorigin: true,    
                  mode: 'cors'    
                });
                const gamedata = await res.json();
                setGamesCount(gamedata);
            } catch (error) {
                setError(error.message)
            } 
        }
        fetchData();

  }

  gameCountFetching();

  

  // render of content or login page
  if(navigation===8){

    return (
      <div className="App" style={{backgroundColor:"#212121", width:"100vw"}}>

        <Login onCreateUser={onCreateUser} status={status} data={data} setData={setData} formSubmit={handleFormSubmit} />
        
      </div>
    );

  } else {
    return (
      <div className="App">
        {!isLoading ? <>
          <Content  onGameSubmit={gameCountFetching} onExit={onExit} navigation={navigation} navigationChange={changeNavigation} username={player} />  
          <Footer gamesCount={gamesCount} online={3} username={player}/> 
        </>
        : 
        <div class="spinner"></div>  
      }
        
      </div>
    );
  }

}

export default App;
