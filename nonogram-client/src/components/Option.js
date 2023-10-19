


import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheckSquare, faGamepad,faDumbbell,faTableList,faWandMagicSparkles,faPeopleGroup, faNetworkWired, faChess } from '@fortawesome/free-solid-svg-icons';

function click(){

}

function Option(props){

    

    let style = "Option";

    if(!props.active)
    style = "Option unactive"

    const click = ()=>{
        if (props.active)
            props.navigationChange(props.type);
    }

    return(
        <div onClick={click} className={style}>
            <div className="Option__text">
            <div className="Option__text__main">{props.optionName}</div>
            <div className="Option__text__descr">{props.descr}</div>
            </div>
            <div className="Option__icon">

            {props.optionName==="Single Game" && 
                <FontAwesomeIcon icon={faGamepad} style={{color: "#7aa9d9",}} />
            }

            {props.optionName==="Training" && 
                <FontAwesomeIcon icon={faDumbbell} style={{color: "#9ad97a",}}/>
            }

            {props.optionName==="Score Table" && 
                <FontAwesomeIcon icon={faTableList} style={{color: "#f5b72a",}}/>
            }

            {props.optionName==="Solver" && 
                <FontAwesomeIcon icon={faWandMagicSparkles} style={{color: "#e73a3a",}}/>
            }

            {/* {(props.optionName==="Create Party" ||  props.optionName==="Connect Party" || props.optionName==="Connect Match") &&
                
            } */}

             {props.optionName==="Create Party"  && 
                <FontAwesomeIcon icon={faPeopleGroup} style={{color: "#9b9b9b",}}/>
             }

            {props.optionName==="Connect Party"  && 
                <FontAwesomeIcon icon={faNetworkWired} style={{color: "#9b9b9b",}}/>
             }


            {props.optionName==="Connect Match"  && 
                <FontAwesomeIcon icon={faChess} style={{color: "#9b9b9b",}}/>
             }


            </div>
        </div>
    );

}

export default Option;