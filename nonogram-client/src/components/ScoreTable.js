
import { Column } from "primereact/column";
import {DataTable} from "primereact/datatable";
import { Button } from 'primereact/button';
import "primereact/resources/primereact.min.css";

import { useState, useEffect } from "react";

const API_URL = "http://127.0.0.1:8080/api/game/scoreTable";

function ScoreTable(props){

    const [gameData,setGameData] = useState([]);
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(true);

    useEffect( ()=>{
        

        async function fetchData(){
            try {
                const res = await fetch(API_URL+"?nickname="+props.username, {    
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

    

        const paginatorLeft = <Button type="button" icon="pi pi-refresh" className="p-button-text" />;
        const paginatorRight = <Button type="button" icon="pi pi-cloud" className="p-button-text" />;
    return(
        <div>
            { !isLoading ? 
                <DataTable
                style={{marginTop:"25px", padding:"15px"}}
                value={gameData}
                paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
                currentPageReportTemplate="Showing {first} to {last} of {totalRecords}" rows={10} rowsPerPageOptions={[10]}
                paginatorLeft={paginatorLeft} paginatorRight={paginatorRight}
                >

                    <Column bodyStyle={{padding:"15px 0", borderBottom:"1px solid black"}} headerStyle={{paddingBottom:"35px"}} style={{textAlign:"left"}} field="partyname" sortable header="Party Name"></Column>
                    <Column bodyStyle={{padding:"15px 0", borderBottom:"1px solid black"}} headerStyle={{paddingBottom:"35px"}} style={{textAlign:"left"}} field="players" sortable header="Players"></Column>
                    <Column bodyStyle={{padding:"15px 0", borderBottom:"1px solid black"}} headerStyle={{paddingBottom:"35px"}} style={{textAlign:"left"}} field="place" sortable header="Place"></Column>
                    <Column bodyStyle={{padding:"15px 0", borderBottom:"1px solid black"}} headerStyle={{paddingBottom:"35px"}} style={{textAlign:"left"}} field="size" sortable header="Size"></Column>
                    <Column bodyStyle={{padding:"15px 0", borderBottom:"1px solid black"}} headerStyle={{paddingBottom:"35px"}} style={{textAlign:"left"}} field="score" sortable header="Score"></Column>
                    <Column bodyStyle={{padding:"15px 0", borderBottom:"1px solid black"}} headerStyle={{paddingBottom:"35px"}} style={{textAlign:"left"}} field="status" sortable header="Status"></Column>
                    <Column bodyStyle={{padding:"15px 0", borderBottom:"1px solid black"}} headerStyle={{paddingBottom:"35px"}} style={{textAlign:"left"}} field="rating" sortable header="Rating"></Column>
                    <Column bodyStyle={{padding:"15px 0", borderBottom:"1px solid black"}} headerStyle={{paddingBottom:"35px"}} style={{textAlign:"left"}} field="date" sortable header="Date"></Column>
                </DataTable>
                :
                <div class="spinner"></div>
            }
        </div>
    );

}
export default ScoreTable;