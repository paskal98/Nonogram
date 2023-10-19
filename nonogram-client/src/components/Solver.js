import React, { useState, useEffect, useCallback } from 'react';
import Input from './Input';
import Nonogram from './Nonogram';
import Button from './Button';

const API_URL = 'http://127.0.0.1:8080/api/game/solve';

function Solver() {
  const [size, setSize] = useState(5);

  const [rowClues, setRowClues] = useState([]);
  const [columnClues, setColumnClues] = useState([]);
  const [grid, setGrid] = useState([]);

  const [gameData,setGameData] = useState([]);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  const resetGridAndClues = (newSize) => {
    const newCluesSize = newSize % 2 === 0 ? newSize / 2 : (newSize + 1) / 2;
    setRowClues(Array(newSize).fill().map(() => Array(newCluesSize).fill(0)));
    setColumnClues(Array(newSize).fill().map(() => Array(newCluesSize).fill(0)));
    setGrid(Array(newSize).fill().map(() => Array(newSize).fill(false)));
  };
  
  useEffect(() => {
    resetGridAndClues(size);
  }, [size]);
  

  const onChange = (data) => {


    if (data[3] === 'row') {
      let tempRowClues = rowClues.map((arr) => [...arr]);
      tempRowClues[data[0]][data[1]] = data[2];
      setRowClues(tempRowClues);
    } else {
      let tempColumnClues = columnClues.map((arr) => [...arr]);
      tempColumnClues[data[0]][data[1]] = data[2];
      setColumnClues(tempColumnClues);
    }
  };

  const onSetSize = (val) => {
    const newSize = parseInt(val);
    setSize(newSize);
    resetGridAndClues(newSize);
  };
  
  

  //api
  const onClick = useCallback(async () => {
    setIsLoading(true);
    setError('');

    const filteredRowClues = rowClues.map(row => row.filter(value => value !== 0 && value !== ''));
    const filteredColumnClues = columnClues.map(row => row.filter(value => value !== 0 && value !== ''));

  
    try {
      const res = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          rowClues: filteredRowClues,
          columnClues: filteredColumnClues,
          size: size,
        }),
      });
      const gamedata = await res.json();
      if (gamedata.length > 2) {
        setGrid(gamedata);
      } else if (gamedata.length == 2){
        setError('Multiply solution');
      }
        else {
        setError('Failed to solve the puzzle. Please check your input.');
      }
    } catch (error) {
      setError(error.message);
    }
    setIsLoading(false);
  }, [rowClues, columnClues, size]);
  

  return (
    <div className="Solver">
      <Input onSetSize={onSetSize} />
      <div className="Solver__content">
        {rowClues.length > 0 && columnClues.length > 0 && grid.length > 1 && (
          <Nonogram
            key={size} 
            onChange={onChange}
            gameField="solver"
            onFillCell={() => {}}
            size={size}
            rowClues={rowClues}
            columnClues={columnClues}
            grid={grid}
        />
        )}
        <Button onClick={onClick} type="ingame" name="Solve" />
        {error && <p className="error-message">{error}</p>}
      </div>
    </div>
  );
}

export default Solver;
