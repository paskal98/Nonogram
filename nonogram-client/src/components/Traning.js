import Nonogram from "./Nonogram";
import GameButtons from "./GameButtons";
import Button from "./Button";
import { useState,useEffect } from "react";
import arraysEqual from "../utils/arraysEqual";

function Traning(){

    let size =5;

    const rowClues = [
        [1,2],
        [1,1],
        [4],
        [1, 1,1],
        [3]
      ];
    
      const columnClues = [
        [1,1],
        [2, 1],
        [1, 3],
        [1,1,1],
        [3]
      ];

      let p0 = Array(size).fill().map(() => Array(size).fill(false));

      let p1 = p0.map((arr)=>{
        return arr.slice();
      })
      p1[3][0]=true;
      p1[3][2]=true;
      p1[3][4]=true;

      let p2 = p1.map((arr)=>{
        return arr.slice();
      })
      p2[0][3]=true;
      p2[2][3]=true;
      p2[4][3]=true;

      let p3 = p2.map((arr)=>{
        return arr.slice();
      })
      p3[0][2]=true;
      p3[2][2]=true;
      p3[4][2]=true;

      let p4 = p3.map((arr)=>{
        return arr.slice();
      })
      p4[4][1]=true;

      let p5 = p4.map((arr)=>{
        return arr.slice();
      })
      p5[2][4]=true;

      let p6 = p5.map((arr)=>{
        return arr.slice();
      })
      p6[2][1]=true;

      let p7 = p6.map((arr)=>{
        return arr.slice();
      })
      p7[1][1]=true;

      let p8 = p7.map((arr)=>{
        return arr.slice();
      })
      p8[1][4]=true;

      let p9 = p8.map((arr)=>{
        return arr.slice();
      })
      p9[0][0]=true;

      let stepByStep = [p0,p1,p2,p3,p4,p5,p6,p7,p8,p9];


      const [grid, setGrid] = useState(Array(size).fill().map(() => Array(size).fill(false)))
      const [permNext, setPermNext] = useState(1);
      const [disableSolution, setDisableSolution] = useState(false);
     
      const onFillCell =  (val)=>{

        let temp = grid.map((arr)=>{
          return arr.slice();
        })
        temp[val[0]][val[1]]=val[2];
        setGrid(temp);

      };


      const onGameButton = (val)=>{

        setDisableSolution(true);

        if(val==="Hint"){
            setGrid(stepByStep[permNext]);
            if(permNext==9) setPermNext(0);
            else setPermNext(permNext+1);
        } else if(val==="Solve"){
            setGrid(stepByStep[9]);
        } else if(val==="Reset"){
            setPermNext(1);
            setGrid(p0);
        } else if(val==="Submit"){
            
        }

      };

      useEffect(() => {
  
        const interval = setInterval(() => {
            if(!disableSolution){
                setGrid(stepByStep[permNext]);
                if(permNext==9) setPermNext(0);
                else setPermNext(permNext+1);
            }
      }, 800);
  
        return () => clearInterval(interval);
    }, [permNext]);
      
      



    return(
        <div className="Traning">

            <div className="Traning__gameinfo">
                <div>
                    <Nonogram onFillCell={onFillCell} size={size} rowClues={rowClues} columnClues={columnClues} grid={grid}/>
                    <GameButtons onGameButton={onGameButton} />
                </div>
                <div className="Traning__info">

                    <div className="Traning__info__clue">
                       <div className="Traning__info__clue__grid">
                            <div className="Traning__info__clue__grid__item">1</div>
                            <div className="Traning__info__clue__grid__item">2</div>
                       </div>

                       <div className="Traning__info__clue__grid Traning__grid__column">
                            <div className="Traning__info__clue__grid__item Traning__item__column">1</div>
                            <div className="Traning__info__clue__grid__item Traning__item__column">1</div>
                       </div>

                      <div className="Traning__info__clue__descr">There are vertical and horizontal clues</div>
                    </div>

                    <div className="Traning__buttons">
                        <div className="Traning__buttons__item"><Button onClick={onGameButton} type="ingame" name="Hint"/> <div className="Traning__buttons__item__descr">Give you a hint, but it reset all field</div> </div>
                        <div className="Traning__buttons__item"><Button onClick={onGameButton} type="ingame" name="Solve"/> <div className="Traning__buttons__item__descr">Solve your nonogram and you didnt resive any rating</div> </div>
                        <div className="Traning__buttons__item"><Button onClick={onGameButton} type="break" name="Reset"/> <div className="Traning__buttons__item__descr">Reset your field, but not time and score</div> </div>
                        <div className="Traning__buttons__item"><Button onClick={onGameButton} type="submit" name="Submit"/> <div className="Traning__buttons__item__descr">Check your game, and save results</div> </div>
                    
                    </div>
                </div>
            </div>

            <div className="Traning__rules">
                

                <h2>Guide</h2>
                <ul>
                <li>Study the grid: Rows and columns have number sequences representing groups of filled cells.</li>
                <li>Start with large numbers: Focus on rows and columns with the largest numbers or sequences.</li>
                <li>Fill cells logically: Fill cells based on clues, leaving at least one empty cell between groups.</li>
                <li>Mark empty cells: Click on cell to mark empty cells, helping to identify patterns.</li>
                <li>Cross-check progress: Ensure filled cell groups match the clues for rows and columns.</li>
                <li>Complete the puzzle: Fill and mark cells until the hidden picture is revealed.</li>
                </ul>
                <br/><br/>

                <p>Nonograms are deceptively simple logic puzzles: You use digits to create a pattern of filled-in squares in the empty grid provided.  Each number on the lines outside the grid represents a block of squares to be blacked out in that row or column (see 1 Across). Once all the correct squares are filled in, a picture emerges. This nonogram adds crossword-inspired clues to make things even more interesting. The answers are numbers that you write into the spaces provided, which then help you find the date-appropriate image.</p>

            </div>

            

        </div>
    );

}

export default Traning;