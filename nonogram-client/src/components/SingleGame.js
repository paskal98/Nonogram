import GameButtons from "./GameButtons";
import Nonogram from "./Nonogram";
import Input from "./Input";
import { useEffect, useState } from "react";
import arraysEqual from "../utils/arraysEqual";
import { Rating } from 'primereact/rating';

import { Messages } from 'primereact/messages';
import { Message } from 'primereact/message';

import Button from "./Button";

const API_BASE = "http://127.0.0.1:8080/api/game";
const API_URL = API_BASE+'/create';

function SingleGame(props){

    const [time, setTime]=useState(Date.now());
  

    //api
    const [size,setSize] = useState(5);

    
    const [gameData,setGameData] = useState([]);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(true);

    const [grid, setGrid] = useState(Array(size).fill().map(() => Array(size).fill(false)))

    const [rowClues, setRowClues] = useState([]);
    const [columnClues, setColumnClues] = useState([]);
    const [solvedGrid, setSolvedGrid] = useState([]);
    const [stepByStep, setStepByStep] = useState([]);
    const [hintIter, setHintIter] = useState(0);
    const [gameStatus, setGameStatus] = useState("SOLVED");
    const [newGame, setNewGame] = useState(true);


    const [submit, setSubmit] = useState("unsubmit");
    const [message, setMessage] = useState(true);
    const [lockGame, setLockGame] = useState(false);

    const [gameFiledId, setGameFiledId] = useState(null);


    useEffect( ()=>{
       
        async function fetchData(){
            try {
                const res = await fetch(API_URL+"?size="+size, {    
                  method: 'GET',     
                  crossorigin: true,    
                  mode: 'cors'    
                });
                const gamedata = await res.json();
                setGameData(gamedata);
            } catch (error) {
                setError(error.message)
            } 
            setIsLoading(false);
        }
        fetchData();

    },[]);

    useEffect(()=>{

      if(gameData.rows!==undefined && gameData.columns!==undefined ){
        setRowClues(gameData.rows);
        setColumnClues(gameData.columns);
        setSolvedGrid(gameData.grid);
        setStepByStep(gameData.stepByStep);
        setIsLoading(false);
      } else {
        setIsLoading(true);
      }

    },[gameData])

    useEffect(()=>{
      
      setIsLoading(true);
      async function fetchData(){
        try {
            const res = await fetch(API_URL+"?size="+size, {    
              method: 'GET',     
              crossorigin: true,    
              mode: 'cors'    
            });
            const gamedata = await res.json();
            setGameData(gamedata);
            setGrid(Array(size).fill().map(() => Array(size).fill(false)));
        } catch (error) {
            setError(error.message)
        } 
        setIsLoading(false);
      }
      fetchData();


    },[size,newGame])


    const onFillCell =  (val)=>{

        if(!lockGame){
          let temp = grid.map((arr)=>{
            return arr.slice();
          })
          temp[val[0]][val[1]]=val[2];
          setGrid(temp);
        }
    };

    const onGameButton = (val)=>{

      if(!lockGame){

        setSubmit("unsubmit")

        if(val==="Hint"){
          if((hintIter+1)<stepByStep.length){
            setHintIter(hintIter+1);

            if((hintIter+1)>=(stepByStep.length-1)){
              setGrid(solvedGrid);
            } else  {
              setGrid(stepByStep[hintIter+1]);
            }

          }
          setGameStatus("CHEATS");
        } else if(val==="Solve"){
          setGrid(solvedGrid);
          setGameStatus("CHEATS");
        } else if(val==="Reset"){
          setHintIter(0);
          setGrid(Array(size).fill().map(() => Array(size).fill(false)));
        } else if(val==="Submit"){

          let isSolved = arraysEqual(grid,solvedGrid);

          if (message===true){
           
            if(isSolved) {
              setSubmit("success");
              setLockGame(true);
            } else {
              setSubmit("unsuccess");
            }
            oneTimeTimer(2500);
           
         }
         setMessage(false);

         if(isSolved){
          sendGameAndSave();
          props.onGameSubmit(props.username)
         }
         

        }

      }
    };


    const sendGameAndSave = async ()=>{

      setIsLoading(true);
      setError('');
  
      try {
        const res = await fetch(API_BASE+"/save", {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            createGameJSON: gameData,
            score:"123",
            gameStatus:gameStatus,
            time:(Date.now()-time)+"",
            size:size,
            player:props.username,
            date:Date.now()
          }),
        });
        const gamedata = await res.json();
        setGameFiledId(gamedata);
      } catch (error) {
        setError(error.message);
      }
      setIsLoading(false);

    };


   
    const oneTimeTimer = (duration) => {
      setTimeout(() => {
        setSubmit("unsubmit");
        setMessage(true);
      }, duration);
    };

    const onSetSize = (val)=>{
       setSize(parseInt(val));
       if(newGame)
        setNewGame(false);
      else
        setNewGame(true);
        setLockGame(false);
        setTime(Date.now());
        setValue(null)
        props.onGameSubmit(props.username)
    };

  
    const [value, setValue] = useState(null);



   const onRating= (rating)=>{
    
      setValue(rating)
      console.log(gameFiledId);
      async function saveRaiting(){
        try {
          const res = await fetch(API_BASE+"/rating", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              fieldId: gameFiledId,
              rating: rating
            }),
          });
          const gamedata = await res.json();
        } catch (error) {
          
        }
      }

      saveRaiting();

    }
 

    return(
        <div className="SingleGame"> 
            <Input onSetSize={onSetSize}/>

            
            <div>
                { !isLoading ?

                ( rowClues.length!==undefined && 
                  columnClues!==undefined && 
                  grid!==undefined && 

                  <>
                  < Nonogram onFillCell={onFillCell} size={size} rowClues={rowClues} columnClues={columnClues} grid={grid}/>
                  <GameButtons onGameButton={onGameButton} />
                  
                  </>
                  ) 

                : (<div class="spinner"></div>)

                }

               
                { lockGame && 
                
                  < >
                  <div class="solved-message">
                    <p>You have solved the game!</p>
                  </div>

                  <div className="stars">
                      <Rating value={value} onChange={(e) => onRating(e.value)} cancel={false} />
                    </div>

                    <div className="fade-in"><Button onClick={()=>onSetSize(size)} type={"ingame"} name={"New?"}/></div>

                    
                  </>
                }

                {submit==="unsuccess" && 
                   <div class="warning-msg">
                      <i class="fa fa-warning"></i>
                      Bad solution!
                   </div> 
                }         

                

            </div>
            
        </div>
    );

}

export default SingleGame;