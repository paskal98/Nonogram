import Clue from "./Clue";

function NongramClues(props){

    const styleType = "NongramClues NongramClues__"+props.type;

    let k =0;
    if (props.type==="row")
        k=100;
    else 
        k=1000

        
    
    if(props.gameField==="solver"){


        let cluesInput=[];

        for(let i=0;i<props.size;i++){
                cluesInput.push( 
                <Clue 
                    onChange={props.onChange} 
                    gameField={props.gameField} 
                    type={props.type} 
                    key={i} 
                    indx={i} 
                    clue={Array( props.size%2==0 ? props.size/2 : ( props.size+1)/2).fill(0)}
                />);
        }

        return <div className={styleType}>{cluesInput}</div>

    }

    return(

        <div className={styleType}>
            { props.clues.map((clue,index)=>{

                return (
                    <Clue gameField={props.gameField} type={props.type} key={k+(1+index)*10} clue={clue}/>
                );

            })}
        </div>

    );

}

export default NongramClues;