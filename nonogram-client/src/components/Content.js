import Describer from "./Describer";
import Header from "./Header";
import Options from "./Options";
import ScoreTable from "./ScoreTable";
import SingleGame from "./SingleGame";
import Traning from "./Traning";
import Solver from "./Solver";


function Content(props){


    return(
        <div>
            <Header onExit={props.onExit} stateName={props.navigation} userData={props} username={props.username} />

            <Describer navigationChange={props.navigationChange} stateName={props.navigation}/>

            { props.navigation==0 && <Options navigationChange={props.navigationChange} />}

            {props.navigation==1 && <SingleGame onRaiting={props.onRaiting} onGameSubmit={props.onGameSubmit} username={props.username}/>}
            {props.navigation==3 && <Traning/>}
            {props.navigation==4 && <ScoreTable username={props.username}/>}
            {props.navigation==7 && <Solver/>}


        </div>
    );

}

export default Content;