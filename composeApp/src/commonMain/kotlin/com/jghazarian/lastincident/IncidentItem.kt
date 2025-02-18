package com.jghazarian.lastincident

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jghazarian.lastincident.util.convertMillisToDate
import com.jghazarian.lastincident.theme.Typography


@Composable
fun IncidentItem(
    item: IncidentEntity,
    onIncidentClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onIncidentClick(item.id) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        //TODO: colors =
        //TODO: elevation = CardDe
    ) {
        Row(
//            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .heightIn(min = 96.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                DetailRow("Title", item.title, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                DetailRow("Date", convertMillisToDate(item.timeStamp), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}

@Composable
fun DetailRow(
    title: String,
    detail: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier)
    {
        Text(
            text = title,
            style = Typography.labelMedium,
        )
        Text(
            text = detail,
            style = Typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp)

        )
    }
}