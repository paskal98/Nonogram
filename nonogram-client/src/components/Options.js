import Option from "./Option";
import MENU_OPTIONS from "../utils/menu";

function Options(props){
    return (
        <div className="Content">
            <Option type={MENU_OPTIONS.SINGLE} navigationChange={props.navigationChange} active={true} optionName="Single Game" descr="one game player"/>
            <Option type={MENU_OPTIONS.CREATE_PARTY} navigationChange={props.navigationChange} active={false} optionName="Create Party" descr="good with freinds"/>
            <Option type={MENU_OPTIONS.TRAINIG} navigationChange={props.navigationChange} active={true} optionName="Training" descr="how play game?"/>
            <Option type={MENU_OPTIONS.SCORE} navigationChange={props.navigationChange} active={true} optionName="Score Table" descr="see your progress"/>
            <Option type={MENU_OPTIONS.CONNECT_PARTY} navigationChange={props.navigationChange} active={false} optionName="Connect Party" descr="connect to freinds"/>
            <Option type={MENU_OPTIONS.CONNECT_MATCH} navigationChange={props.navigationChange} active={false} optionName="Connect Match" descr="ranking game"/>
            <Option type={MENU_OPTIONS.SOLVER} navigationChange={props.navigationChange} active={true} optionName="Solver" descr="solve your puzzle"/>
        </div>
    );
}

export default Options;